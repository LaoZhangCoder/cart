<template>
  <div class="hello">
    <HeaderCommon></HeaderCommon>
    <div style="width:90% ">
      <div v-for="good in goods" class="gl-i-wrap j-sku-item">
        <div class="p-img">
          <a target="_blank">
            <img width="220" height="220" data-img="1"
                 src="//img14.360buyimg.com/n7/jfs/t1/96505/26/15113/79912/5e6ec8a5Eb6d8546c/0cc2d2120993ffaf.jpg">
          </a>
        </div>
        <div class="p-price">
          <strong class="J_price"><em>¥</em><i>{{good.goodsPrice}}</i></strong></div>
        <div class="p-name">
          <a>
            <em style="font-style:normal; font-weight: normal;">
              {{good.goodsName}} </em>
          </a>
        </div>
        <a class="p-o-btn addCart"><i></i>加入购物车</a>
      </div>
    </div>
  </div>

</template>
<script>
  import HeaderCommon from './HeaderCommon'
  export default {
    name: 'app',
    components:{
      HeaderCommon
    },
    data() {
      return {
        goods: [],

      }
    },
    created() {
      this.$ajax.get("/api/goods/list").then((response) => {
        this.goods = response.data.result;
      })
    }


  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

  .gl-i-wrap {
    width: 240px;
    height: 476px;
    float: left;
    margin-top: 3%;
    margin-left: 8%;

  }

  .p-price {
    color: #e4393c;
    font-weight: 400;
    font-family: Verdana;
    font-weight: bold;
  }

  .p-name {
    height: 40px;
    margin-bottom: 8px;
    overflow: hidden;
    word-break: break-all;
    word-wrap: break-word;
    font-style: normal;
    color: #666;
    text-decoration: none;
  }

  .addCart {
    width: 67px;
    text-align: center;
    color: #e4393c;
  }

  .goods-list-v2 .gl-item .p-operate .p-o-btn {
    float: left;
    height: 25px;
    line-height: 25px;
    border: 1px solid #DDD;
    padding: 0 3px 0 24px;
    position: relative;
    background: #fff;
    color: #999;
  }

</style>

