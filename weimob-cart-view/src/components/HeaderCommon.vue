<template>
  <div>
    <div class="header">
      <div class="login" v-on:click="dialogFormVisible = true">{{userName}}</div>
      <router-link to="/cart">
        <div class="myCart" >我的购物车</div>
      </router-link>
      <div class="logout" v-on:click="logout" >退出登录</div>
    </div>
    <el-dialog title="欢迎登录！" :visible.sync="dialogFormVisible">
      <el-form :model="form" :rules="rules" ref="form" >
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
        }else if (!(/^1[34578]\d{9}$/.test(value))) {
          return callback(new Error('请输入正确的手机号码'));
        }else{
          callback();
        }
      }
      var checkPassword = (rule, value, callback) => {
        if (!value) {
          return callback(new Error('密码不能为空!'));
        }else {
          callback();
        }
      }
      return {
        currentDate: new Date(),
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
            { validator: checkPhoneNumber, trigger: 'blur' }
          ],
          password: [
            { validator: checkPassword, trigger: 'blur' }
          ]
        }
      }
    },
    created(){
      if(Cookies.get("userInfo")==null){
        this.userName="你好! 请登录"
      }else{
        var userName=Cookies.get("userInfo");
        this.userName="weimob"+userName
      }
    },
    methods: {
      submitForm(formName) {
        this.dialogFormVisible=true
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.dialogFormVisible=false
            this.$ajax.post("/api/user/login",this.form).then((response)=>{
              if(response.data.success) {
                Cookies.set("userInfo", response.data.result.userId + ":" + response.data.result.userName,{expires: 1})
                this.userName="weimob"+Cookies.get("userInfo");
              }else{
                this.dialogFormVisible=true
                alert("用户名或者密码错误!")

              }

            })
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      logout(){
        Cookies.remove("userInfo")
        location.reload();
      },
      resetForm(formName) {
        this.dialogFormVisible=false
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
    background-color:#e3e4e5;
  }
  .login{
    float: left;
    margin-left: 40%;
    margin-right: 10%;
    line-height: 40px;
    color:  #999;
    div:hove

  }
  .myCart{
    float: left;
    line-height: 40px;
    cursor: pointer;
    color: red;
  }
  .logout{
    float: left;
    margin-left: 20%;
    line-height: 40px;
    cursor: pointer;
    color: red;
  }
  .login:hover
  {
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

</style>

