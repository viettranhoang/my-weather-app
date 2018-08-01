package com.vit.myweatherapp.logger;

import timber.log.Timber;


public class NotLoggingTree extends Timber.Tree {
    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
    }
}
