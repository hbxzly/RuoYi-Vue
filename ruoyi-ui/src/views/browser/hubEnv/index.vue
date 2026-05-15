<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="环境ID" prop="containerCode">
        <el-input
          v-model="queryParams.containerCode"
          placeholder="请输入环境ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="环境序号" prop="serialNumber">
        <el-input
          v-model="queryParams.serialNumber"
          placeholder="请输入环境序号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="环境名称" prop="containerName">
        <el-input
          v-model="queryParams.containerName"
          placeholder="请输入环境名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="分组名称" prop="tagName">
        <el-input
          v-model="queryParams.tagName"
          placeholder="请输入环境分组名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="账号名称" prop="accountName">
        <el-input
          v-model="queryParams.accountName"
          placeholder="请输入账号名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="平台名称" prop="platformName">
        <el-input
          v-model="queryParams.platformName"
          placeholder="请输入平台名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="分组ID" prop="tagCode">
        <el-input
          v-model="queryParams.tagCode"
          placeholder="请输入环境分组ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="代理类型" prop="proxyTypeName">
        <el-input
          v-model="queryParams.proxyTypeName"
          placeholder="请输入代理类型"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="代理主机" prop="proxyHost">
        <el-input
          v-model="queryParams.proxyHost"
          placeholder="请输入代理主机"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="代理端口" prop="proxyPort">
        <el-input
          v-model="queryParams.proxyPort"
          placeholder="请输入代理端口"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="代理账号" prop="proxyAccount">
        <el-input
          v-model="queryParams.proxyAccount"
          placeholder="请输入代理账号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="代理密码" prop="proxyPassword">
        <el-input
          v-model="queryParams.proxyPassword"
          placeholder="请输入代理密码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="上一次使用的IP" prop="lastUsedIp">
        <el-input
          v-model="queryParams.lastUsedIp"
          placeholder="请输入上一次使用的IP"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="上一次IP国家" prop="lastCountry">
        <el-input
          v-model="queryParams.lastCountry"
          placeholder="请输入上一次IP国家"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="上一次IP省/州" prop="lastRegion">
        <el-input
          v-model="queryParams.lastRegion"
          placeholder="请输入上一次IP省/州"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="上一次IP城市" prop="lastCity">
        <el-input
          v-model="queryParams.lastCity"
          placeholder="请输入上一次IP城市"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="最后打开时间" prop="openTime">
        <el-date-picker clearable
          v-model="queryParams.openTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择最后打开时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="环境最后打开时间" prop="allOpenTime">
        <el-input
          v-model="queryParams.allOpenTime"
          placeholder="请输入环境最后打开时间"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
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
          v-hasPermi="['browser:hubEnv:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['browser:hubEnv:edit']"
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
          v-hasPermi="['browser:hubEnv:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['browser:hubEnv:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-refresh"
          size="mini"
          @click="handleGetHubEnvList"
        >更新列表</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="hubEnvList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="id" />
      <el-table-column label="环境ID" align="center" prop="containerCode" />
      <el-table-column label="环境序号" align="center" prop="serialNumber" />
      <el-table-column label="环境名称" align="center" prop="containerName" />
      <el-table-column label="平台名称" align="center" prop="platformName" />
      <el-table-column label="账号名称" align="center" prop="accountName" />
<!--      <el-table-column label="环境分组名称" align="center" prop="tagName" />
      <el-table-column label="环境分组ID" align="center" prop="tagCode" />
      <el-table-column label="代理使用方式：1-静态，2-动态" align="center" prop="asDynamicType" />-->
      <el-table-column label="代理类型" align="center" prop="proxyTypeName" />
      <el-table-column label="代理主机" align="center" prop="proxyHost" />
      <el-table-column label="代理端口" align="center" prop="proxyPort" />
      <el-table-column label="代理账号" align="center" prop="proxyAccount" />
      <el-table-column label="代理密码" align="center" prop="proxyPassword" />
<!--      <el-table-column label="上一次使用的IP" align="center" prop="lastUsedIp" />-->
<!--      <el-table-column label="上一次IP国家" align="center" prop="lastCountry" />-->
<!--      <el-table-column label="上一次IP省/州" align="center" prop="lastRegion" />-->
<!--      <el-table-column label="上一次IP城市" align="center" prop="lastCity" />-->
<!--      <el-table-column label="浏览器UA" align="center" prop="ua" />-->
<!--      <el-table-column label="刷新URL" align="center" prop="refreshUrl" />-->
<!--      <el-table-column label="最后打开时间" align="center" prop="openTime" width="180">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ parseTime(scope.row.openTime, '{y}-{m}-{d}') }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="环境最后打开时间" align="center" prop="allOpenTime" />-->
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['browser:hubEnv:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['browser:hubEnv:remove']"
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

    <!-- 添加或修改Hubstudio 环境对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="环境ID" prop="containerCode">
          <el-input v-model="form.containerCode" placeholder="请输入环境ID" />
        </el-form-item>
        <el-form-item label="环境序号" prop="serialNumber">
          <el-input v-model="form.serialNumber" placeholder="请输入环境序号" />
        </el-form-item>
        <el-form-item label="环境名称" prop="containerName">
          <el-input v-model="form.containerName" placeholder="请输入环境名称" />
        </el-form-item>
        <el-form-item label="平台名称" prop="platformName">
          <el-input v-model="form.platformName" placeholder="请输入平台名称" />
        </el-form-item>
        <el-form-item label="账号名称" prop="accountName">
          <el-input v-model="form.accountName" placeholder="请输入账号名称" />
        </el-form-item>
<!--        <el-form-item label="环境分组名称" prop="tagName">
          <el-input v-model="form.tagName" placeholder="请输入环境分组名称" />
        </el-form-item>
        <el-form-item label="环境分组ID" prop="tagCode">
          <el-input v-model="form.tagCode" placeholder="请输入环境分组ID" />
        </el-form-item>-->
        <el-form-item label="代理类型" prop="proxyTypeName">
          <el-input v-model="form.proxyTypeName" placeholder="请输入代理类型" />
        </el-form-item>
        <el-form-item label="代理主机" prop="proxyHost">
          <el-input v-model="form.proxyHost" placeholder="请输入代理主机" />
        </el-form-item>
        <el-form-item label="代理端口" prop="proxyPort">
          <el-input v-model="form.proxyPort" placeholder="请输入代理端口" />
        </el-form-item>
        <el-form-item label="代理账号" prop="proxyAccount">
          <el-input v-model="form.proxyAccount" placeholder="请输入代理账号" />
        </el-form-item>
        <el-form-item label="代理密码" prop="proxyPassword">
          <el-input v-model="form.proxyPassword" placeholder="请输入代理密码" />
        </el-form-item>
<!--        <el-form-item label="上一次使用的IP" prop="lastUsedIp">-->
<!--          <el-input v-model="form.lastUsedIp" placeholder="请输入上一次使用的IP" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="上一次IP国家" prop="lastCountry">-->
<!--          <el-input v-model="form.lastCountry" placeholder="请输入上一次IP国家" />
        </el-form-item>
        <el-form-item label="上一次IP省/州" prop="lastRegion">
          <el-input v-model="form.lastRegion" placeholder="请输入上一次IP省/州" />
        </el-form-item>
        <el-form-item label="上一次IP城市" prop="lastCity">
          <el-input v-model="form.lastCity" placeholder="请输入上一次IP城市" />
        </el-form-item>
        <el-form-item label="浏览器UA" prop="ua">
          <el-input v-model="form.ua" type="textarea" placeholder="请输入内容" />
        </el-form-item>-->
        <el-form-item label="刷新URL" prop="refreshUrl">
          <el-input v-model="form.refreshUrl" type="textarea" placeholder="请输入内容" />
        </el-form-item>
<!--        <el-form-item label="最后打开时间" prop="openTime">
          <el-date-picker clearable
            v-model="form.openTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择最后打开时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="环境最后打开时间" prop="allOpenTime">
          <el-input v-model="form.allOpenTime" placeholder="请输入环境最后打开时间" />
        </el-form-item>-->
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listHubEnv, getHubEnv, delHubEnv, addHubEnv, updateHubEnv, getHubEnvList } from "@/api/browser/hubEnv";

export default {
  name: "HubEnv",
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
      // Hubstudio 环境表格数据
      hubEnvList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        containerCode: null,
        serialNumber: null,
        containerName: null,
        accountName: null,
        platformName: null,
        tagName: null,
        tagCode: null,
        asDynamicType: null,
        proxyTypeName: null,
        proxyHost: null,
        proxyPort: null,
        proxyAccount: null,
        proxyPassword: null,
        lastUsedIp: null,
        lastCountry: null,
        lastRegion: null,
        lastCity: null,
        ua: null,
        refreshUrl: null,
        openTime: null,
        allOpenTime: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        containerCode: [
          { required: true, message: "环境ID不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询Hubstudio 环境列表 */
    getList() {
      this.loading = true;
      listHubEnv(this.queryParams).then(response => {
        this.hubEnvList = response.rows;
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
        id: null,
        containerCode: null,
        serialNumber: null,
        containerName: null,
        accountName: null,
        platformName: null,
        tagName: null,
        tagCode: null,
        asDynamicType: null,
        proxyTypeName: null,
        proxyHost: null,
        proxyPort: null,
        proxyAccount: null,
        proxyPassword: null,
        lastUsedIp: null,
        lastCountry: null,
        lastRegion: null,
        lastCity: null,
        ua: null,
        refreshUrl: null,
        openTime: null,
        allOpenTime: null,
        createTime: null,
        createBy: null,
        remark: null
      };
      this.resetForm("form");
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加Hubstudio 环境";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getHubEnv(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改Hubstudio 环境";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateHubEnv(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addHubEnv(this.form).then(response => {
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
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除Hubstudio 环境编号为"' + ids + '"的数据项？').then(function() {
        return delHubEnv(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('browser/hubEnv/export', {
        ...this.queryParams
      }, `hubEnv_${new Date().getTime()}.xlsx`)
    },
    handleGetHubEnvList(){
        getHubEnvList().then(response => {
          this.getList();
        })
    }
  }
};
</script>
