package com.sapient.backend.model;

import lombok.Getter;

@Getter
public enum Suggestion {

    CARRY_UMBRELLA("Carry umbrella"),
    USE_SUN_SCREEN("Use sunscreen lotion"),
    WINDY("It’s too windy, watch out!"),
    THUNDERSTORM("Don’t step out! A Storm is brewing!");

    private String suggestionText;

    Suggestion(String suggestionText) {
        this.suggestionText = suggestionText;
    }
}
