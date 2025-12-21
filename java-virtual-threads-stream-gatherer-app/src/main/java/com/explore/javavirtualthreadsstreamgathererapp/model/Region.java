package com.explore.javavirtualthreadsstreamgathererapp.model;

import java.util.List;

public record Region(int id,
                     String name,
                     List<Integer> subRegions) {
}