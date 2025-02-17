/*
 *  Copyright 2024-2025 Dataport. All rights reserved. Developed as part of the POSSIBLE project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package eu.possiblex.participantportal.business.entity.common;

public class CommonConstants {

    /**
     * Endpoint for creating service offers WITHOUT data.
     */
    public static final String REST_PATH_FH_CATALOG_SERVICE_OFFER = "/trust/service-offering";

    /**
     * Endpoint for creating service offers WITH data.
     */
    public static final String REST_PATH_FH_CATALOG_SERVICE_OFFER_WITH_DATA = "/trust/data-product";

    private CommonConstants() {
        // Utility class
    }

}
