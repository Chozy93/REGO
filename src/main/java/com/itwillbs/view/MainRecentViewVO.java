package com.itwillbs.view;

import com.itwillbs.dto.MainRecentViewDTO;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class MainRecentViewVO {

    private final List<MainRecentItemVO> items;

    public MainRecentViewVO(List<MainRecentViewDTO> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            this.items = Collections.emptyList();
            return;
        }

        this.items = dtos.stream()
                .map(MainRecentItemVO::new)
                .toList();
    }

    public static MainRecentViewVO empty() {
        return new MainRecentViewVO(Collections.emptyList());
    }
}
