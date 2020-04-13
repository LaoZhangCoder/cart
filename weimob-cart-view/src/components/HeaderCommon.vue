<template>
  <div>
    <div class="header">
      <div class="login" v-on:click="dialogFormVisible = true">{{userName}}</div>
      <router-link to="/cart">
        <div class="cw-icon">
          <svg t="1586243805783" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4431" width="24" height="24"><path d="M871.3 401.8H290.1c-26.7 0-48.3-21.6-48.3-48.3s21.6-48.3 48.3-48.3h581.2c26.7 0 48.3 21.6 48.3 48.3s-21.6 48.3-48.3 48.3z" fill="#DA2048" opacity=".8" p-id="4432"></path><path d="M795.2 740.8h-445c-26.7 0-48.3-21.6-48.3-48.3s21.6-48.3 48.3-48.3h445c26.7 0 48.3 21.6 48.3 48.3s-21.6 48.3-48.3 48.3z" fill="#DA2048" opacity=".8" p-id="4433"></path><path d="M350.2 740.8c-23 0-43.3-16.5-47.5-39.8L242.6 362l-21.3-133.1c-4.2-26.3 13.7-51.1 40-55.3 26.3-4.2 51.1 13.7 55.3 40L337.8 346l59.9 338.2c4.6 26.2-12.9 51.3-39.1 55.9-2.8 0.5-5.7 0.7-8.4 0.7z" fill="#DA2048" opacity=".8" p-id="4434"></path><path d="M795.3 740.8c-3.5 0-7.1-0.4-10.6-1.2-26-5.8-42.4-31.6-36.5-57.7l76.1-339c5.8-26 31.7-42.3 57.7-36.5 26 5.8 42.4 31.6 36.5 57.7l-76.1 339c-5.1 22.5-25 37.7-47.1 37.7z" fill="#DA2048" opacity=".8" p-id="4435"></path><path d="M435.2 906.1c-38.3 0-69.4-31.2-69.4-69.4s31.2-69.4 69.4-69.4c38.3 0 69.4 31.2 69.4 69.4s-31.1 69.4-69.4 69.4z m0-96.5c-14.9 0-27.1 12.1-27.1 27.1 0 14.9 12.1 27.1 27.1 27.1 14.9 0 27.1-12.1 27.1-27.1 0-15-12.2-27.1-27.1-27.1z" fill="#DA2048" opacity=".8" p-id="4436"></path><path d="M726 906.1c-38.3 0-69.4-31.2-69.4-69.4s31.2-69.4 69.4-69.4c38.3 0 69.4 31.2 69.4 69.4s-31.1 69.4-69.4 69.4z m0-96.5c-14.9 0-27.1 12.1-27.1 27.1 0 14.9 12.1 27.1 27.1 27.1 14.9 0 27.1-12.1 27.1-27.1 0-15-12.2-27.1-27.1-27.1z" fill="#DA2048" opacity=".8" p-id="4437"></path><path d="M268.9 269.4H118.7c-26.7 0-48.3-21.6-48.3-48.3s21.6-48.3 48.3-48.3h150.2c26.7 0 48.3 21.6 48.3 48.3s-21.6 48.3-48.3 48.3z" fill="#DA2048" opacity=".8" p-id="4438"></path></svg>
          <a target="_blank">我的购物车</a>
          <i class="ci-count" id="shopping-amount">{{cartCount}}</i>
        </div>
      </router-link>
      <div class="logout" v-on:click="logout">退出登录</div>
    </div>
    <el-dialog title="欢迎登录！" :visible.sync="dialogFormVisible">
      <el-form :model="form" :rules="rules" ref="form">
        <el-form-item label="手机号" prop="phoneNumber" :label-width="formLabelWidth">
          <el-input v-model="form.phoneNumber" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password" :label-width="formLabelWidth">
          <el-input v-model="form.password" autocomplete="off" type="password"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="submitForm('form')">确 定</el-button>
        <el-button type="primary" @click="resetForm('form')">取 消</el-button>
      </div>
    </el-dialog>
  </div>

</template>
<script>
  import Cookies from 'js-cookie';

  export default {
    name: 'HeaderCommon',
    data() {
      var checkPhoneNumber = (rule, value, callback) => {
        if (!value) {
          return callback(new Error('手机号码不能为空!'));
        } else if (!(/^1[34578]\d{9}$/.test(value))) {
          return callback(new Error('请输入正确的手机号码'));
        } else {
          callback();
        }
      }
      var checkPassword = (rule, value, callback) => {
        if (!value) {
          return callback(new Error('密码不能为空!'));
        } else {
          callback();
        }
      }
      return {
        currentDate: new Date(),
        cartCount: 0,
        goods: [],
        userName: '你好, 请登录!',
        dialogFormVisible: false,
        form: {
          phoneNumber: '',
          password: '',
          date1: '',
          date2: '',
          delivery: false,
          type: [],
          resource: '',
          desc: ''
        },
        formLabelWidth: '120px',
        rules: {
          phoneNumber: [
            {validator: checkPhoneNumber, trigger: 'blur'}
          ],
          password: [
            {validator: checkPassword, trigger: 'blur'}
          ]
        }
      }
    },
    created() {
      this.init();
      if (Cookies.get("userInfo") == null) {
        this.userName = "你好! 请登录"
      } else {
        var userName = Cookies.get("userInfo");
        this.userName = "weimob" + userName
      }
    },

    methods: {
      init() {
        this.$ajax.get("/api/cart/list").then((response) => {
          let res = response.data;
          this.cartCount = res.result.length;
        })
      },
      submitForm(formName) {
        this.dialogFormVisible = true
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.dialogFormVisible = false
            this.$ajax.post("/api/user/login", this.form).then((response) => {
              if (response.data.success) {
                location.reload();
              } else {
                this.dialogFormVisible = true
                alert("用户名或者密码错误!")

              }

            })
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      logout() {
        Cookies.remove("userInfo")
        location.reload();
      },
      resetForm(formName) {
        this.dialogFormVisible = false
        this.$refs[formName].resetFields();
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .header {
    height: 40px;
    width: 100%;
    background-color: #e3e4e5;
  }

  .login {
    float: left;
    margin-left: 40%;
    margin-right: 10%;
    line-height: 40px;
    color: #999;
    div: hove

  }

  .myCart {
    float: left;
    line-height: 40px;
    cursor: pointer;
    color: red;
    border-radius: 1px 1px 1px 2px;
    border-color: red;
  }

  .logout {
    float: left;
    margin-left: 20%;
    line-height: 40px;
    cursor: pointer;
    color: red;
  }

  .login:hover {
    color: red;
    cursor: pointer;
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

  .cw-icon {
    width: 130px;
    height: 34px;
    background-color: #fff;
    text-align: center;
    line-height: 34px;
    overflow: hidden;
    position: relative;
    z-index: 1;
    float: left;
    border: 1px solid #e3e4e5;
  }
   .cw-icon .iconfont,  .cw-icon a {
    color: #e1251b;
    -webkit-transition: color .2s ease;
    transition: color .2s ease;
  }
   .cw-icon .iconfont {
    margin-right: 13px;
    font-size: 16px;
  }
  .iconfont {
    font-family: iconfont,sans-serif;
    font-style: normal;
    -webkit-text-stroke-width: .2px;
    -moz-osx-font-smoothing: grayscale;
  }
   .cw-icon {
    width: 130px;
    height: 34px;
    background-color: #fff;
    text-align: center;
    line-height: 34px;
    border-color: #eee;
  }
   .cw-icon .iconfont, #settleup .cw-icon a {
    color: #e1251b;
    -webkit-transition: color .2s ease;
    transition: color .2s ease;
  }
  a {
    color: #666;
    text-decoration: none;

  }
   .ci-count {
    position: absolute;
    top: 1px;
    left: 29px;
    right: auto;
    display: inline-block;
    padding: 1px 3px;
    font-size: 12px;
    line-height: 12px;
    color: #fff;
    background-color: #e1251b;
    border-radius: 7px;
    min-width: 12px;
    text-align: center;
  }
  em, i {
    font-style: normal;
  }
  .icon{
    height: 34px;
    float: left;

  }
</style>

