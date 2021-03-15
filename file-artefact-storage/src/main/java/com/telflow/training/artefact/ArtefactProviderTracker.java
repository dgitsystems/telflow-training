package com.telflow.training.artefact;

import com.telflow.artefact.api.ArtefactstoreProvider;
import com.telflow.fabric.core.trackers.MappingTelflowServiceTracker;

/**
 * Class to track Artefact Providers
 *
 */
public class ArtefactProviderTracker extends MappingTelflowServiceTracker<String, ArtefactstoreProvider> {

    public ArtefactProviderTracker() {
        super(ArtefactstoreProvider.class, ArtefactstoreProvider::getProviderId);
    }

}
