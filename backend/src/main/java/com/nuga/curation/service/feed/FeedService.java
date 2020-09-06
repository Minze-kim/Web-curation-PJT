package com.nuga.curation.service.feed;

import java.util.*;

public interface FeedService {
    public List<String> feedList(Long userId);
    public void addFeed(Long userId,List<String> feedList);
}
