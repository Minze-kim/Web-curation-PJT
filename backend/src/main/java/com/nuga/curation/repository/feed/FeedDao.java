package com.nuga.curation.repository.feed;

import com.nuga.curation.domain.enterestfeed.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface FeedDao extends JpaRepository<Feed,Long> {
    Feed findFeedByFeedName(String feedName);
}
