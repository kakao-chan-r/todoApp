package com.kakaoix.common;

import lombok.Data;

@Data
public class PageConfig {
    private int page;  		    // 보여줄 페이지 번호
    private int perPageNum;     // 페이지당 보여줄 게시글의 개수
    private int totalCount;     // 게시판 전체 데이터 개수

    private int displayPageNum = 10;   // 게시판 화면에서 한번에 보여질 페이지 번호의 개수 (1,2,3,4,5,6,7,9,10)
    private int startPage;      // 현재 화면에서 보이는 startPage 번호
    private int endPage;        // 현재 화면에 보이는 endPage 번호
    private boolean prev;       // 페이징 이전 버튼 활성화 여부
    private boolean next;       // 페이징 다음 버튼 활서화 여부
    private int limitStartPage;      // limit

    public PageConfig(final int page, final int perPageNum, final int displayPageNum, final int totalCount) {
        this.page = page;
        this.perPageNum = perPageNum;
        this.totalCount = totalCount;
        this.displayPageNum = displayPageNum;
        this.limitStartPage = (page - 1) * perPageNum;
        pageSet();
    }

    public void pageSet() {
        endPage = (int) (Math.ceil(page / (double) displayPageNum) * displayPageNum);

        startPage = (endPage - displayPageNum) + 1;

        int tempEndPage = (int) (Math.ceil(totalCount / (double) perPageNum));

        if(endPage > tempEndPage) {
            endPage = tempEndPage;
        }

        prev = startPage == 1 ? false : true;

        next = endPage * perPageNum >= totalCount ? false : true;
    }
}
