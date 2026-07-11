<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="80px">
      <el-form-item label="设备名称" prop="deviceName">
        <el-input
          v-model="queryParams.deviceName"
          placeholder="请输入设备名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备版本" prop="deviceVersion">
        <el-input
          v-model="queryParams.deviceVersion"
          placeholder="请输入设备版本"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="包名" prop="packageName">
        <el-input
          v-model="queryParams.packageName"
          placeholder="请输入包名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="账号ID" prop="createAccountId">
        <el-input
          v-model="queryParams.createAccountId"
          placeholder="请输入账号ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建日期" prop="createDate">
        <el-date-picker
          v-model="queryParams.createDate"
          clearable
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择创建日期"
        />
      </el-form-item>
      <el-form-item label="备注" prop="note">
        <el-input
          v-model="queryParams.note"
          placeholder="请输入备注"
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
          v-hasPermi="['account:device:add']"
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['account:device:edit']"
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['account:device:remove']"
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['account:device:export']"
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['account:device:edit']"
          type="warning"
          plain
          icon="el-icon-refresh"
          size="mini"
          @click="handleGetDevices"
        >获取</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-view"
          size="mini"
          @click="handleViewOnlineDevices"
        >查看在线设备</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-document"
          size="mini"
          :disabled="multiple"
          @click="handleChangeNote"
        >修改备注</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="deviceList" @selection-change="handleSelectionChange" @sort-change="handleSortChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="keyId" width="80" sortable="custom" :sort-orders="['descending', 'ascending']" />
      <el-table-column label="设备名称" align="center" prop="deviceName" show-overflow-tooltip />
      <el-table-column label="设备版本" align="center" prop="deviceVersion" width="100" />
      <el-table-column label="包名" align="center" prop="packageName" show-overflow-tooltip />
      <el-table-column label="账号ID" align="center" prop="createAccountId" show-overflow-tooltip />
      <el-table-column label="日期" align="center" prop="createDate" width="120">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="note" show-overflow-tooltip />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['account:device:edit']"
            size="mini"
            type="text"
            icon="el-icon-video-play"
            @click="handleOpen(scope.row)"
          >打开</el-button>
          <el-button
            v-hasPermi="['account:device:edit']"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            v-hasPermi="['account:device:remove']"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
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

    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="设备名称" prop="deviceName">
          <el-input v-model="form.deviceName" placeholder="请输入设备名称" />
        </el-form-item>
        <el-form-item label="设备版本" prop="deviceVersion">
          <el-input v-model="form.deviceVersion" placeholder="请输入设备版本" />
        </el-form-item>
        <el-form-item label="包名" prop="packageName">
          <el-input v-model="form.packageName" placeholder="请输入包名" />
        </el-form-item>
        <el-form-item label="账号ID" prop="createAccountId">
          <el-input v-model="form.createAccountId" placeholder="请输入创建账号ID" />
        </el-form-item>
        <el-form-item label="创建日期" prop="createDate">
          <el-date-picker
            v-model="form.createDate"
            clearable
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择创建日期"
          />
        </el-form-item>
        <el-form-item label="备注" prop="note">
          <el-input v-model="form.note" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="title" :visible.sync="openChangeNote" width="500px" append-to-body>
      <el-form ref="changeNoteForm" :model="changeNoteFormData" size="medium" label-width="100px">
        <el-form-item label="备注" prop="note">
          <el-input v-model="changeNoteFormData.note" placeholder="备注" clearable />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="closeChangeNote">取 消</el-button>
        <el-button type="primary" @click="submitChangeNote">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog
      title="在线设备"
      :visible.sync="onlineDevice.open"
      width="520px"
      append-to-body
      @closed="resetOnlineDevice"
    >
      <el-table
        v-loading="onlineDevice.loading"
        :data="onlineDevice.list"
        border
        stripe
        empty-text="暂无在线设备"
      >
        <el-table-column label="序号" type="index" width="80" align="center" />
        <el-table-column label="设备名称" align="center" prop="deviceName" show-overflow-tooltip />
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="onlineDevice.open = false">关 闭</el-button>
        <el-button type="primary" :loading="onlineDevice.loading" @click="loadOnlineDevices">刷 新</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listDevice, getDevice, delDevice, addDevice, updateDevice, getDevices, getOnlineDevices, openDevice, changeNote } from '@/api/account/device'

export default {
  name: 'Device',
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      openChangeNote: false,
      onlineDevice: {
        open: false,
        loading: false,
        list: []
      },
      changeNoteFormData: {
        keyId: null,
        note: ''
      },
      total: 0,
      deviceList: [],
      title: '',
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        deviceName: null,
        deviceVersion: null,
        packageName: null,
        createAccountId: null,
        createDate: null,
        note: null
      },
      form: {},
      rules: {}
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listDevice(this.queryParams).then(response => {
        this.deviceList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        keyId: null,
        deviceName: null,
        deviceVersion: null,
        packageName: null,
        createAccountId: null,
        createDate: null,
        note: null
      }
      this.resetForm('form')
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.keyId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleSortChange(column) {
      this.queryParams.orderByColumn = column.prop
      this.queryParams.isAsc = column.order
      this.getList()
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加创建设备'
    },
    handleUpdate(row) {
      this.reset()
      const keyId = row.keyId || this.ids
      getDevice(keyId).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改创建设备'
      })
    },
    handleOpen(row) {
      openDevice(row)
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.form.keyId != null) {
            updateDevice(this.form).then(() => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addDevice(this.form).then(() => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const keyIds = row.keyId || this.ids
      this.$modal.confirm('是否确认删除创建设备编号为"' + keyIds + '"的数据项？').then(function() {
        return delDevice(keyIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleExport() {
      this.download('account/device/export', {
        ...this.queryParams
      }, `device_${new Date().getTime()}.xlsx`)
    },
    handleGetDevices() {
      getDevices().then(response => {
        this.$modal.msgSuccess(response.msg || '获取成功')
        this.getList()
      })
    },
    handleViewOnlineDevices() {
      this.onlineDevice.open = true
      this.loadOnlineDevices()
    },
    loadOnlineDevices() {
      this.onlineDevice.loading = true
      getOnlineDevices().then(response => {
        const devices = response.data || []
        this.onlineDevice.list = devices.map(deviceName => ({ deviceName }))
      }).finally(() => {
        this.onlineDevice.loading = false
      })
    },
    resetOnlineDevice() {
      this.onlineDevice.loading = false
      this.onlineDevice.list = []
    },
    handleChangeNote() {
      this.title = '修改备注'
      this.openChangeNote = true
    },
    resetChangeNote() {
      this.changeNoteFormData = {
        keyId: null,
        note: ''
      }
      this.resetForm('changeNoteForm')
    },
    closeChangeNote() {
      this.openChangeNote = false
      this.resetChangeNote()
    },
    submitChangeNote() {
      const keyIds = this.ids
      const note = this.changeNoteFormData.note
      this.openChangeNote = false
      changeNote(keyIds, note).then(() => {
        this.$modal.msgSuccess('修改成功')
        this.resetChangeNote()
        this.getList()
      })
    }
  }
}
</script>
