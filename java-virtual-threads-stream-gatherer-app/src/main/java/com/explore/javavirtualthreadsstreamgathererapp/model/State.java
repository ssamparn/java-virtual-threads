package com.explore.javavirtualthreadsstreamgathererapp.model;

import java.util.List;

public record State(int id,
                    int countryId,
                    String name,
                    String stateCode,
                    List<Integer> cities) {
}