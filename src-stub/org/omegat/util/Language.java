/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2000-2006 Keith Godfrey and Maxym Mykhalchuk
               2007 Didier Briel, Zoltan Bartko
               2010-2011 Didier Briel
               2012 Guido Leenders
               Home page: http://www.omegat.org/
               Support center: http://groups.yahoo.com/group/OmegaT/

 This file is part of OmegaT.

 OmegaT is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 OmegaT is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **************************************************************************/

package org.omegat.util;

import java.util.Locale;

/**
 * This class is here, because the Locale has hard-coded '_' inside, and we must
 * adhere to ISO standard LL-CC.
 * <p>
 * This class tries to follow <a
 * href="http://www.lisa.org/standards/tmx/tmx.html#xml:lang">TMX Specification
 * on languages</a>, which is based on <a
 * href="http://www.ietf.org/rfc/rfc3066.txt">RFC 3066</a>, i.e.
 * <ul>
 * <li>Language is composed from 1-8 alpha (A-Za-z) chars, then "-", then 1-8
 * alpha/digit chars (A-Za-z0-9).
 * <li>Case insensitive
 * <li>Case is not altered by this class, even though there exist conventions
 * for capitalization ([ISO 3166] recommends that country codes are capitalized
 * (MN Mongolia), and [ISO 639] recommends that language codes are written in
 * lower case (mn Mongolian)).
 * <ul>
 * 
 * @author Maxym Mykhalchuk
 * @author Didier Briel
 * @author Zoltan Bartko bartkozoltan@bartkozoltan.com
 * @author Guido Leenders
 */
public class Language {
    public Locale getLocale() {return null;}
    public String getCountryCode() {return null;}
    public String getLocaleCode() {return null;}
    public String getLanguageCode() {return null;}
}
