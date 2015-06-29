/**************************************************************************
# TransvisionForOmegaT - Plugin for OmegaT which get the translations
# 	          from Transvision Webservice. 
#
# Copyright (C) 2014 Enrique Estevez (keko.gl@gmail.com)
#               Home page: 
#
# This file is part of TransvisionForOmegaT.
#
# TransvisionForOmegaT is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# TransvisionForOmegaT is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#**************************************************************************/

package org.omegat.plugins.machinetranslators;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import java.net.URLEncoder;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.prefs.Preferences;
import javax.swing.JMenuItem;

import org.omegat.core.Core;
import org.omegat.core.machinetranslators.BaseTranslate;
import org.omegat.core.data.SourceTextEntry;
import org.omegat.util.Language;
import org.omegat.util.StaticUtils;
import org.omegat.util.WikiGet;

/**
 *
 * @author Enrique Estevez
 */
public class TransvisionTranslate extends BaseTranslate {

    protected static String GT_URL = "http://transvision.mozfr.org/?sourcelocale=#sourceLang#&locale=#targetLang#&repo=#repo#&search_type=entities&recherche=";
    protected static String GT_S = "http://transvision.mozfr.org/?sourcelocale=#sourceLang#&locale=#targetLang#&repo=#repo#&search_type=strings&perfect_match=perfect_match&recherche=";
    //protected static String GT_E = "&search_type=entities&whole_word=whole_word&recherche=";
    protected static String GT_URL2 = "&json"; //NOI18N
    protected static String MARK_BEG = "\":\""; //NOI18N
    protected static String MARK_END = "\"}"; //NOI18N
    protected static Pattern RE_UNICODE = Pattern.compile("\\\\u([0-9A-Fa-f]{4})"); //NOI18N
    protected static Pattern RE_HTML = Pattern.compile("&#([0-9]+);"); //NOI18N
    protected static Pattern RE_DETAILS = Pattern.compile("\"responseDetails\":\"([^\"]+)"); //NOI18N
    protected static Pattern RE_STATUS = Pattern.compile("\"responseStatus\":([0-9]+)"); //NOI18N

    public TransvisionTranslate() {
        JMenuItem item = new JMenuItem(transvision.getString("TRANSVISION_SETTINGS"));
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TransvisionSettingsDialog(null, true).setVisible(true);
            }
        });
        Core.getMainWindow().getMainMenu().getMachineTranslationMenu().add(item);
        //Core.getMainWindow().getMainMenu().getOptionsMenu().add(item);
    }
    
    @Override
    protected String getPreferenceName() {
        return "allow_transvision_translate_plugin";
    }
    
    @Override
    public String getName() {
        return transvision.getString("TRANSVISION_NAME");
    }
    
    @Override
    protected String translate(Language sLang, Language tLang, String text) throws Exception {
        SourceTextEntry currentEntry = Core.getEditor().getCurrentEntry();
        // A marca de inicio MARK_BEG podemos axustala a traves do ficheiro, da chave, e da cadea
        // orixe para obter a traducion exacta noutro idioma para o que buscamos
        String searchesList = prefs.get("locales", null);
        String url;
        String translations = "";
        String entity = URLEncoder.encode(currentEntry.getKey().file + ":" + currentEntry.getKey().id, "UTF-8");        
        String [] list = searchesList.split(";");
        for(String pair : list)
        {
            String [] datos = pair.split(":");
            if (datos.length==2){
                if ( datos[0].equals("mozilla_org") )
                {
                    String aux = GT_S.replace("#sourceLang#", "en-GB");
                    url = aux.replace("#repo#", datos[0]).replace("#targetLang#", datos[1]);
                    translations += datos[0] + ":" + datos[1] + "\t-  ";
                    translations += getTranslation(url + convertCharacters(URLEncoder.encode(text, "UTF-8")) + GT_URL2,"nada") + "\n";
                }
                else {
                    String aux = GT_URL.replace("#sourceLang#", "en-US");
                    url = aux.replace("#repo#", datos[0]).replace("#targetLang#", datos[1]);
                    translations += datos[0] + ":" + datos[1] + "\t-  ";
                    translations += getTranslation(url + entity + GT_URL2,"nada") + "\n";
                }
            }
        }
        return translations;
    }
    
    private String convertCharacters(String url) {
        
        // Before: Reminder - Replace & ---> (%26) by %26amp%3B
        // url = url.replace("%26","%26amp%3B");
        
        // Before: Reminder - Replace < ---> (%3C) by %26lt%3B
        // url = url.replace("%3C","%26lt%3B");
        // Now: No neccesary - Replace < ---> (%3C) by <
        // url = url.replace("%3C","<"); 
        
        // Before: Reminder - Replace > ---> (%3E) by %26gt%3B
        // url = url.replace("%3E","%26gt%3B");
        // Now: No neccesary - Replace > ---> (%3E) by >
        //url = url.replace("%3E",">");
        
        // Before: Reminder - Replace " ---> (%22) by %26quot%3B
        // url = url.replace("%22", "%26quot%3B");
        // Now: No neccesary - Replace " ---> (%22) by "
        //url = url.replace("%22","\"");
        
        return url;
    }

    private String getTranslation(String searchUrl, String strPattern) throws Exception {

        String v;
        try {
            v = WikiGet.getURL(searchUrl);
        } catch (IOException e) {
            return e.getLocalizedMessage();
        }

        while (true) {
            Matcher m = RE_UNICODE.matcher(v);
            if (!m.find()) {
                break;
            }
            String g = m.group();
            char c = (char) Integer.parseInt(m.group(1), 16);
            v = v.replace(g, Character.toString(c));
        }

        v = v.replace("&quot;", "&#34;");
        v = v.replace("&nbsp;", "&#160;");
        v = v.replace("&amp;", "&#38;");
        v = v.replace("\\\"", "\"");

        while (true) {
            Matcher m = RE_HTML.matcher(v);
            if (!m.find()) {
                break;
            }
            String g = m.group();
            char c = (char) Integer.parseInt(m.group(1));
            v = v.replace(g, Character.toString(c));
        }

        int beg = v.indexOf(MARK_BEG) + MARK_BEG.length();
        int end = v.indexOf(MARK_END, beg);
        if (end < 0) {
            //no translation found. e.g. {"responseData":{"translatedText":null},"responseDetails":"Not supported pair","responseStatus":451}
            Matcher m = RE_DETAILS.matcher(v);
            if (!m.find()) {
                return "";
            }
            String details = m.group(1);
            String code = "";
            m = RE_STATUS.matcher(v);
            if (m.find()) {
                code = m.group(1);
            }
            return StaticUtils.format(transvision.getString("TRANSVISION_ERROR"), code, details);
        }
        String tr = v.substring(beg, end);
        return tr;
    } 

    protected static final Preferences prefs = Preferences.userNodeForPackage(TransvisionTranslate.class);
    private static final ResourceBundle transvision = ResourceBundle.getBundle("org/omegat/Transvision");
    
}