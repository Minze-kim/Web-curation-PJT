package com.nuga.curation.service.article;

import com.nuga.curation.domain.article.dto.ArticleDto;
import com.nuga.curation.domain.article.dto.TagRequest;
import com.nuga.curation.domain.article.entity.Article;
import com.nuga.curation.domain.article.entity.ArticleTag;
import com.nuga.curation.domain.category.Category;
import com.nuga.curation.domain.follow.Follow;
import com.nuga.curation.domain.image.Image;
import com.nuga.curation.domain.user.dto.SearchUserResponse;
import com.nuga.curation.domain.user.entity.User;
import com.nuga.curation.domain.wish.Wish;
import com.nuga.curation.repository.article.ArticleDao;
import com.nuga.curation.repository.article.ArticleTagDao;
import com.nuga.curation.repository.article.CategoryDao;
import com.nuga.curation.repository.article.ImageDao;
import com.nuga.curation.repository.user.FollowDao;
import com.nuga.curation.repository.user.UserDao;
import com.nuga.curation.repository.wish.WishDao;
import com.nuga.curation.service.tag.TagService;
import com.nuga.curation.service.tag.TagServiceImpl;
import com.nuga.curation.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.digester.ArrayStack;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService{

    private final Long HASH_KEY = 1000000L;
    private final UserDao userDao;
    private final ArticleDao articleDao;
    private final CategoryDao categoryDao;
    private final ModelMapper modelMapper;
    private final FollowDao followDao;
    private final WishDao wishDao;
    private final TagService tagService;
    private final ImageDao imageDao;
    private final ArticleTagDao articleTagDao;

    public Long writeArticle(Long userId, ArticleDto.ArticleRequest articleRequest,
                             List<TagRequest> tagRequests, String categoryName, List<ArticleDto.ImageDto> imageRequests) throws ParseException {
        tagService.addTagList(tagRequests);
        User seller = userDao.findById(userId).get();
        Category category = categoryDao.getCategoryByCategoryName(categoryName);
        Article saveArticle = articleRequest.toEntity()
                .sellerSetting(seller)
                .categorySetting(category);

        Date startDate = new SimpleDateFormat("yyyy-MM-dd")
                .parse(articleRequest.getPurchaseDate());
        LocalDateTime purchaseDate = startDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        saveArticle.setPurchaseDate(purchaseDate);

        saveArticle.expireDateSetting(articleRequest.getExpireDate());

        Long id = articleDao.save(saveArticle.createDateSetting(LocalDateTime.now())).getArticleId();
        tagService.addTagToArticle(saveArticle,tagRequests);
        imageAddUrl(saveArticle,imageRequests);
        return id;
    }

    @Override
    @Transactional
    public Long writeArticleReceiver(Long userId,ArticleDto.ArticleWriteRequest request) throws ParseException {
        return writeArticle(userId,request.getArticleRequest(),request.getTagRequests(),request.getCategoryName(),request.getImages());
    }

    //비로그인 시 아티클리스트
    @Override
    public List<ArticleDto.ArticleResponse> articleList() {
        List<Article> allInfo = articleDao.findAllByOrderByCreatedDateDesc();
        return allInfo.stream()
                .map(this::articleMapping)
                .collect(Collectors.toList());
    }

    //로그인 시 아티클리스트
    @Override
    public List<ArticleDto.ArticleResponse> articleList(Long userId,String categoryName) {
        User loginUser = userDao.findById(userId).get();
        List<ArticleDto.ArticleResponse> articleResponses = new ArrayList<ArticleDto.ArticleResponse>();
        List<ArticleDto.ArticleResponse> tail = new ArrayList<ArticleDto.ArticleResponse>();
        HashSet<Long> fInfo = new HashSet<Long>();
        List<Follow> followInfo =followDao.getFollowsByFollowInfo_User(loginUser);
        for(Follow f : followInfo){
            fInfo.add(userId*HASH_KEY + f.getFollowInfo().getTarget().getUser_id());
        }
        List<Article> allList = articleDao.findAll();
        for(Article a : allList){
            Long sellerId = a.getSeller().getUser_id();
            Long key = userId * HASH_KEY + sellerId;
            if(fInfo.contains(key)){
                if(isWish(a,loginUser)){
                    articleResponses.add(articleMapping(a).setWished(true));
                }else{
                    articleResponses.add(articleMapping(a).setWished(false));
                }
            }else{
                if(isWish(a,loginUser)){
                    tail.add(articleMapping(a).setWished(true));
                }else{
                    tail.add(articleMapping(a).setWished(false));
                }

            }
        }

        Collections.sort(articleResponses, (o1, o2) -> o2.getCreatedDate().compareTo(o1.getCreatedDate()));
        Collections.sort(tail, (o1, o2) -> o2.getCreatedDate().compareTo(o1.getCreatedDate()));

        articleResponses.addAll(tail);
        if(categoryName!=null && categoryName!=""){
            articleResponses = categoryFilter(articleResponses,categoryName);
        }
        return articleResponses;
    }

    @Override
    public ArticleDto.ArticleResponse articleDetail(Long articleId,Long userId) {
        Article articleInfo = articleDao.findById(articleId).get();
        ArticleDto.ArticleResponse response = articleMapping(articleInfo);
        if(userId==null) return response; //로그인 유저가 아닐경우 밑에 과정 필요 없음
        User loginUser = userDao.findById(userId).get();
        if(isFollow(articleInfo.getSeller(),loginUser))response.setFollowed(true);
        if(isWish(articleInfo,loginUser))response.setWished(true);
        if(articleInfo.getSeller()==loginUser)response.setFollowed(false);
        return response;
    }

    @Override
    public List<ArticleDto.ArticleResponse> myArticles(String nickName) {
        User user = userDao.findUserByNickName(nickName);
        List<Article> articleList = articleDao.findArticlesBySellerOrderByExpireDateDesc(user);
        List<ArticleDto.ArticleResponse> list = articleList.stream().map(this::articleMapping).collect(Collectors.toList());
        wishSetting(list,user);
        return list;
    }

    @Override
    public ArticleDto.SearchResponse searchArticle(String keyword,Long loginId) {
        //유저리스트
        List<SearchUserResponse> searchUser = new ArrayList<SearchUserResponse>();
        User loginUser = null;
        if(loginId!=null) loginUser = userDao.findById(loginId).get();
        List<User> userList = userDao.findUserByNickNameContaining(keyword);
        for(User u : userList){
            SearchUserResponse response = SearchUserResponse.builder()
                    .nickName(u.getNickName())
                    .email(u.getEmail())
                    .build();
            if(loginUser!=null && isFollow(u,loginUser)){
                response.setFollowed(true);
            }
            searchUser.add(response);
        }
        //아티클리스트
        List<ArticleDto.ArticleResponse> searchArticle = new ArrayList<ArticleDto.ArticleResponse>();
        List<Article> articleList = articleDao.findSearchArticle(keyword);
        searchArticle = articleList.stream().map(this::articleMapping).collect(Collectors.toList());
        wishSetting(searchArticle,loginUser);
        return new ArticleDto.SearchResponse(searchUser,searchArticle);
    }

    @Override
    public boolean deleteArticle(Long articleId) {
        Article delete = articleDao.findById(articleId).get();
        articleDao.delete(delete);
        return true;
    }

    @Override
    public List<ArticleDto.ArticleResponse> categoryArticles(String categoryName) {
        return articleDao.findArticlesByCategory(categoryName)
                .stream()
                .map(this::articleMapping)
                .collect(Collectors.toList());

    }

    private ArticleDto.ArticleResponse articleMapping(Article articleInfo) {
        ArticleDto.ArticleResponse response = modelMapper.map(articleInfo,ArticleDto.ArticleResponse.class);
        response.setSellerNickname(articleInfo.getSeller().getNickName());
        response.setSellerId(articleInfo.getSeller().getUser_id());

        response.setTags(tagsToArticle(articleInfo));
        response.setImages(imageToString(imageDao.findImageByArticle(articleInfo)));

        User buyer = articleInfo.getBuyer();
        Category category= articleInfo.getCategory();

        if(buyer!=null) response.setBuyerNickname(buyer.getNickName());
        if(category!=null) response.setCategoryName(category.getCategoryName());
        return response;
    }

    public SearchUserResponse userMapping(User user){
       return modelMapper.map(user,SearchUserResponse.class);
    }

    public boolean isWish(Article article,User user){
        Wish wish = wishDao.getWishByUserAndArticle(user,article);
        if(wish==null)return false;
        return true;
    }

    public boolean isFollow(User user, User targetUser){
        if(followDao.getFollowByFollowInfo_UserAndFollowInfo_Target(user,targetUser)!=null ||
                followDao.getFollowByFollowInfo_UserAndFollowInfo_Target(targetUser,user) !=null) return true;
        return false;
    }


    @Transactional
    public void wishSetting(List<ArticleDto.ArticleResponse> article, User loginUser){
        if(loginUser == null) return;
        for(ArticleDto.ArticleResponse a : article){
            if(isWish(articleDao.findById(a.getArticleId()).get(),loginUser)){
                a.setWished(true);
            }else{
                a.setWished(false);
            }
        }
    }
    //아티클에 해당하는 이미지 정보 넣기
    @Transactional
    public void imageAddUrl(Article article, List<ArticleDto.ImageDto> requests){
        for(ArticleDto.ImageDto img : requests){
            Image image = Image.builder().article(article).urlPath(img.getUrlPath()).build();
            imageDao.save(image);
        }
    }
    public List<String> imageToString(List<Image> list){
        List<String> ret = new ArrayList<String>();
        for(Image i: list){
            ret.add(i.getUrlPath());
        }
        return ret;
    }

    public List<String> tagsToArticle(Article article){
        List<ArticleTag> list = articleTagDao.getArticleTagsByArticle(article);
        List<String> tags = new ArrayList<String>();
        for(ArticleTag articleTag : list){
            tags.add(articleTag.getTag().getTagName());
        }
        return tags;
    }

    public List<ArticleDto.ArticleResponse> categoryFilter(List<ArticleDto.ArticleResponse> list,String categoryName){
        list = list
                .stream()
                .filter(s->s.getCategoryName()!=null && s.getCategoryName().equals(categoryName))
                .collect(Collectors.toList());
        return list;
    }
}
