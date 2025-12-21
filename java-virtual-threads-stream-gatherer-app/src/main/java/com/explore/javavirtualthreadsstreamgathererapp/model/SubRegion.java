package com.explore.javavirtualthreadsstreamgathererapp.model;

import java.util.List;

public record SubRegion(int id,
                        int regionId,
                        String name,
                        List<Integer> countries) {
}