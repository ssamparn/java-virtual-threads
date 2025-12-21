package com.explore.javavirtualthreadsstreamgathererapp.model;

import java.util.List;

public record Country(int id,
                      int subRegionId,
                      String name,
                      List<Integer> states) {
}