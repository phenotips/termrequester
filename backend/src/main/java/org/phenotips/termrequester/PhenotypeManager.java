/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/
 */
package org.phenotips.termrequester;

import org.phenotips.termrequester.github.GithubAPI;

import java.nio.file.Path;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Optional;

/**
 * Manages the lifecycle of a requested (or existing) phenotype within the entire system.
 * Serves as a facade to the various aspects of the termrequester backend.
 * Keeps the phenotype synchronized accross services every time it is accessed.
 * @version $Id$
 */
public interface PhenotypeManager
{
    /**
     * Initialize this manager.
     * @param repo the repository in use.
     * @param home the path to the directory where this object can live
     * @throws TermRequesterBackendException on initialization failure
     */
    void init(GithubAPI.Repository repo, Path home) throws TermRequesterBackendException;

    /**
     * Request a new phenotype in the HPO.
     * @param name the name of the phenotype to request
     * @param synonyms a list of synonyms for the phenotype
     * @param parentIds the phenotype's parents
     * @param description optionally the phenotype's description
     * @return the newly created request, and whether or not it was new
     * @throws TermRequesterBackendException if something goes wrong in the backend.
     */
    PhenotypeCreation createRequest(String name, Collection<String> synonyms, Collection<String> parentIds,
                                    Optional<String> description) throws TermRequesterBackendException;

    /**
     * Get the phenotype with the id given. This might be an HPO or a termrequester id.
     * @param id the id of the phenotype.
     * @return the phenotype, if it's there. The null phenotype if it is not.
     * @throws TermRequesterBackendException if something goes wrong in the backend.
     */
    Phenotype getPhenotypeById(String id) throws TermRequesterBackendException;

    /**
     * Fuzzily search for phenotypes matching the text given.
     * @param text the text to search for
     * @return the list of phenotypes
     * @throws TermRequesterBackendException if something goes wrong in the backend.
     */
    List<Phenotype> search(String text) throws TermRequesterBackendException;

    /**
     * A response to the createRequest method, containing the phenotype and whether
     * or not it was just created.
     *
     * @version $Id$
     */
    class PhenotypeCreation
    {
        /**
         * The phenotype.
         */
        public Phenotype phenotype;

        /**
         * Whether it is new.
         */
        public boolean isNew;

        public PhenotypeCreation(Phenotype phenotype, boolean isNew)
        {
            this.phenotype = phenotype;
            this.isNew = isNew;
        }
    }
}
