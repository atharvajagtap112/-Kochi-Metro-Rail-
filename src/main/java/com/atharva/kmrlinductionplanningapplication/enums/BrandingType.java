package com.atharva.kmrlinductionplanningapplication.enums;

public enum BrandingType {
    FULL_WRAP("Full Train Wrap"),
    PARTIAL_WRAP("Partial Train Wrap"),
    SIDE_PANEL("Side Panel Branding"),
    DOOR_WRAP("Door Wrap"),
    WINDOW_STICKERS("Window Stickers"),
    GRAB_HANDLES("Grab Handle Branding"),
    CEILING_POSTERS("Ceiling Posters"),
    FLOOR_STICKERS("Floor Stickers"),
    SEAT_COVERS("Seat Cover Branding"),
    DIGITAL_DISPLAYS("Digital Display Ads"),
    EXTERIOR_PANELS("Exterior Panel Ads"),
    INTERIOR_POSTERS("Interior Poster Ads");

    private final String description;

    BrandingType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
