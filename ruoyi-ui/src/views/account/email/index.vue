<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="邮箱" prop="email">
        <el-input
          v-model="queryParams.email"
          placeholder="请输入邮箱"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input
          v-model="queryParams.password"
          placeholder="请输入密码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="账号ID" prop="accountId">
        <el-input
          v-model="queryParams.accountId"
          placeholder="请输入账号ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否绑定账号" prop="isBoundAccount">
        <el-select v-model="queryParams.isBoundAccount" placeholder="请选择状态；0否，1是" clearable>
          <el-option
            v-for="dict in dict.type.is_bound_account"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态；0被释放，1正常，2被锁，3未知异常" clearable>
          <el-option
            v-for="dict in dict.type.email_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="备注" prop="note">
        <el-input
          v-model="queryParams.note"
          placeholder="请输入备注"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="最近一次消息" prop="lastTimeMessage">
        <el-input
          v-model="queryParams.lastTimeMessage"
          placeholder="请输入最近一次消息"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['account:email:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="upload"
          size="mini"
          @click="handleImport"
          v-hasPermi="['account:email:add']"
        >上传</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['account:email:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['account:email:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['account:email:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUnlock"
          v-hasPermi="['account:email:remove']"
        >解锁</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          @click="handleTempLogin"
          v-hasPermi="['account:email:remove']"
        >临时登录</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="emailList" @selection-change="handleSelectionChange" height="800">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="keyId" />
      <el-table-column label="邮箱" align="center" prop="email" />
      <el-table-column label="密码" align="center" prop="password" />
      <el-table-column label="账号ID" align="center" prop="accountId" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.email_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="是否绑定账号" align="center" prop="isBoundAccount">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.is_bound_account" :value="scope.row.isBoundAccount"/>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="note" />
      <el-table-column label="最近一次消息" align="center" prop="lastTimeMessage" show-overflow-tooltip>
        <template slot-scope="scope">
          <span @dblclick="showMessageDialog(scope.row.lastTimeMessage)">
             {{ scope.row.lastTimeMessage }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleGetMessage(scope.row)"
            v-hasPermi="['account:email:edit']"
          >取件</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['account:email:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUnlock(scope.row)"
            v-hasPermi="['account:email:edit']"
          >解锁</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['account:email:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改email对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="账号ID" prop="accountId">
          <el-input v-model="form.accountId" placeholder="请输入账号ID" />
        </el-form-item>
        <el-form-item label="是否绑定账号" prop="isBoundAccount">
          <el-radio-group v-model="form.isBoundAccount">
            <el-radio
              v-for="dict in dict.type.is_bound_account"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.email_status"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="note">
          <el-input v-model="form.note" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="最近一次消息" prop="lastTimeMessage">
          <el-input v-model="form.lastTimeMessage" placeholder="请输入最近一次消息" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- email导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <div class="el-upload__tip" slot="tip">
            <el-checkbox v-model="upload.updateSupport" /> 是否更新已经存在的用户数据
          </div>
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>

    <!--显示消息-->
    <el-dialog
      title="最近一次消息"
      :visible.sync="messageDialog.visible"
      width="500px"
      append-to-body
    >
      <p>{{ messageDialog.content }}</p>
      <span slot="footer" class="dialog-footer">
    <el-button @click="messageDialog.visible = false">关闭</el-button>
  </span>
    </el-dialog>

    <!-- 解锁对话框 -->
    <el-dialog title="选择解锁方式" :visible.sync="unlockDialogVisible" width="400px">
      <el-form ref="unlockForm" :model="unlockForm" >
        <el-form-item label="解锁邮箱" prop="email">
          {{unlockForm.email}}
        </el-form-item>
        <el-form-item label="解锁方式">
          <el-radio-group v-model="unlockForm.unlockType">
            <el-radio :label="1">一般解锁</el-radio>
            <el-radio :label="2">添加号码</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="unlockDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmUnlock">确定</el-button>
      </template>
    </el-dialog>

    <!--邮箱临时登录-->
    <el-dialog :title="title" :visible.sync="tempLoginDialog" width="500px" append-to-body>
      <el-form ref="tempLoginForm" :model="tempEmailData"  size="medium" label-width="100px">
        <el-form-item label="邮箱" prop="tempEmail">
          <el-input v-model="tempEmailData.tempEmail" placeholder="账号-密码" clearable :style="{width: '100%'}">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="closeTempLoginDialog">取消</el-button>
        <el-button type="primary" @click="submitTempLoginDialog">确定</el-button>
      </div>
    </el-dialog>

  </div>
</template>


<script>
import { listEmail, getEmail, delEmail, addEmail, updateEmail, getMessage, unlockEmail, tempEmailLogin, loginEmail } from "@/api/account/email";
import { getToken } from "@/utils/auth";
import item from "@/layout/components/Sidebar/Item.vue";

export default {
  name: "Email",
  dicts: ['email_status', 'is_bound_account'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // email表格数据
      emailList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      tempLoginDialog: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        email: null,
        accountId: null,
        password: null,
        status: null,
        isBoundAccount: null,
        note: null,
        lastTimeMessage: null
      },

      // email导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/account/email/importData"
      },

      // 文件上传中处理
      handleFileUploadProgress(event, file, fileList) {
        this.upload.isUploading = true;
      },
      // 表单参数
      form: {},

      tempEmailData: {
        tempEmail: ""
      },

      unlockForm: {
        email: "", // 解锁邮箱
        unlockType: 1, // 默认 "一般解锁"
      },
      // 表单校验
      rules: {
      },

      messageDialog: {
        visible: false,
        content: ''
      },

      unlockDialogVisible: false, // 控制对话框的显示
      unlockType: 1, // 默认选项 (1: 一般解锁, 2: 添加号码)
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询email列表 */
    getList() {
      this.loading = true;
      listEmail(this.queryParams).then(response => {
        this.emailList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        keyId: null,
        email: null,
        accountId: null,
        password: null,
        status: null,
        isBoundAccount: null,
        note: null,
        lastTimeMessage: null
      };
      this.resetForm("form");
    },

    resetTempEmailData(){
      this.$refs.tempLoginForm.resetFields()
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.keyId)
      this.selectEmail = selection.map(item => item.email)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },

    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加email";
    },

    /**解锁邮箱*/
    /*handleUnlock(row) {
      const keyIds = row.keyId || this.ids;
      this.$modal.confirm('是否确认解锁email编号为"' + keyIds + '"的数据项？').then(function() {
        return unlockEmail(keyIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("解锁成功");
      }).catch(() => {});
    },*/

    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "email导入";
      this.upload.open = true;
    },

    /** 下载模板操作 */
    importTemplate() {
      this.download('account/email/importTemplate', {
      }, `email_template_${new Date().getTime()}.xlsx`)
    },

    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },

    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
    /** 修改按钮操作 */
    handleGetMessage(row) {
      this.reset();
      // 组合 row 对象
      const requestData = {
        keyId: row.keyId,
        email: row.email,
        password: row.password,
        status: row.status,
        note: row.note,
        lastTimeMessage: row.lastTimeMessage
      };
      getMessage(requestData).then(response => {
        this.$modal.msgSuccess("获取成功");
        this.getList();
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const keyId = row.keyId || this.ids
      getEmail(keyId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改email";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.keyId != null) {
            updateEmail(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addEmail(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const keyIds = row.keyId || this.ids;
      this.$modal.confirm('是否确认删除email编号为"' + keyIds + '"的数据项？').then(function() {
        return delEmail(keyIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('account/email/export', {
        ...this.queryParams
      }, `email_${new Date().getTime()}.xlsx`)
    },


    showMessageDialog(message) {
      this.messageDialog.content = message;
      this.messageDialog.visible = true;
    },

    // 点击解锁按钮，打开对话框
    handleUnlock(row) {
      this.unlockForm = {
        email: row.email || this.selectEmail || "", // 默认邮箱
        unlockType: 1, // 重置解锁方式
      };
      this.unlockDialogVisible = true;
    },

    handleTempLogin(){
      this.title = "临时邮箱";
      this.tempLoginDialog = true
    },



    confirmUnlock(){
      //打印选中的邮箱，所选的项目
      this.unlockDialogVisible = false;
      unlockEmail(this.unlockForm.email,this.unlockForm.unlockType);
    },

    closeTempLoginDialog(){
      this.resetTempEmailData();
      this.tempLoginDialog = false
    },

    submitTempLoginDialog(){
      const keyIds =this.ids;

      if (keyIds && keyIds.length > 0) {
        // keyIds 不为空
        loginEmail(keyIds);
      } else {
        // 原逻辑
        tempEmailLogin(this.tempEmailData.tempEmail);
      }
      this.closeTempLoginDialog()
    }

  }

};
</script>

