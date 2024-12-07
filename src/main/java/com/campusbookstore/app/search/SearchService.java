package com.campusbookstore.app.search;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {


    String viewSearchPost () {
        return "search/search";
    }
}
