/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2010 Alex Buloichik
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
package org.omegat.core.data;

/**
 * Class for store full entry's identifier, including file, id, src, etc.
 * 
 * @author Alex Buloichik (alex73mail@gmail.com)
 */
public class EntryKey implements Comparable<EntryKey> {

    public final String file;
    public final String sourceText;
    public final String id;
    public final String prev;
    public final String next;
    public final String path;
    
    public EntryKey(final String file, final String sourceText, final String id, final String prev,
            final String next, final String path) {
        this.file = file;
        this.sourceText = sourceText;
        this.id = id;
        this.prev = prev;
        this.next = next;
        this.path = path;
    }
        
    @Override
    public int compareTo(EntryKey o) {return 0;}

    @Override
    public String toString() {return null;}
    
}
