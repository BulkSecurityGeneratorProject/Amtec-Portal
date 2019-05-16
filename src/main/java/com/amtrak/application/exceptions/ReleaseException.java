package com.amtrak.application.exceptions;

public class ReleaseException {
    public static class ProductionReleaseExists extends Exception {
        public ProductionReleaseExists(String errorMessage) {
            super(errorMessage);
        }
        public ProductionReleaseExists(String errorMessage, Throwable err) {
            super(errorMessage, err);
        }
    }
    public static class ProductionIsFuture extends Exception {
        public ProductionIsFuture(String errorMessage) {
            super(errorMessage);
        }
        public ProductionIsFuture(String errorMessage, Throwable err) {
            super(errorMessage, err);
        }
    }
}
