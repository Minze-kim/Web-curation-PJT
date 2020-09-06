import baseApi from './BaseApi'
export default {

  BASE_URL: baseApi.BASE_URL,

  ROUTES: {
    createarticle: '/articles',
    fetcharticles: '/articles/all',
    fetchcategoryarticle: '/articles/category/', //{categoryName},
    detailarticle: '/articles/detail/', //{articleId}
    myarticle: '/articles/',
    search: '/articles/search/'
  }
}