package com.ruoyi.web.controller.account;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.account.domain.CreateInfo;
import com.ruoyi.account.domain.FbAccountForSell;
import com.ruoyi.account.service.ICreateInfoService;
import io.appium.java_client.AppiumDriver;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.account.domain.CreateDevice;
import com.ruoyi.account.service.ICreateDeviceService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 创建设备Controller
 * 
 * @author ruoyi
 * @date 2025-07-15
 */
@RestController
@RequestMapping("/account/device")
public class CreateDeviceController extends BaseController
{
    @Autowired
    private ICreateDeviceService createDeviceService;

    @Autowired
    private ICreateInfoService createInfoService;

    /**
     * 查询创建设备列表
     */
    @PreAuthorize("@ss.hasPermi('account:device:list')")
    @GetMapping("/list")
    public TableDataInfo list(CreateDevice createDevice)
    {
        startPage();
        List<CreateDevice> list = createDeviceService.selectCreateDeviceList(createDevice);
        return getDataTable(list);
    }

    /**
     * 导出创建设备列表
     */
    @PreAuthorize("@ss.hasPermi('account:device:export')")
    @Log(title = "创建设备", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CreateDevice createDevice)
    {
        List<CreateDevice> list = createDeviceService.selectCreateDeviceList(createDevice);
        ExcelUtil<CreateDevice> util = new ExcelUtil<CreateDevice>(CreateDevice.class);
        util.exportExcel(response, list, "创建设备数据");
    }

    /**
     * 获取创建设备详细信息
     */
    @PreAuthorize("@ss.hasPermi('account:device:query')")
    @GetMapping(value = "/{keyId}")
    public AjaxResult getInfo(@PathVariable("keyId") Long keyId)
    {
        return success(createDeviceService.selectCreateDeviceByKeyId(keyId));
    }

    /**
     * 新增创建设备
     */
    @PreAuthorize("@ss.hasPermi('account:device:add')")
    @Log(title = "创建设备", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CreateDevice createDevice)
    {
        return toAjax(createDeviceService.insertCreateDevice(createDevice));
    }

    /**
     * 修改创建设备
     */
    @PreAuthorize("@ss.hasPermi('account:device:edit')")
    @Log(title = "创建设备", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CreateDevice createDevice)
    {
        return toAjax(createDeviceService.updateCreateDevice(createDevice));
    }

    /**
     * 删除创建设备
     */
    @PreAuthorize("@ss.hasPermi('account:device:remove')")
    @Log(title = "创建设备", businessType = BusinessType.DELETE)
	@DeleteMapping("/{keyIds}")
    public AjaxResult remove(@PathVariable Long[] keyIds)
    {
        return toAjax(createDeviceService.deleteCreateDeviceByKeyIds(keyIds));
    }

    @GetMapping("/getDevices")
    @ResponseBody
    public AjaxResult getDevices(){

        try {
            // 1. 获取设备名
            String deviceName = null;
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "adb devices");
            builder.redirectErrorStream(true);
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), "GBK"));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.endsWith("\tdevice")) {
                    deviceName = line.split("\t")[0].trim();  // 取出 emulator-5554
                    break;
                }
            }
            process.waitFor();

            if (deviceName == null) {
                System.out.println("未找到任何设备");
            }

            System.out.println("获取到设备名: " + deviceName);

            // 2. 获取所有 package
            ProcessBuilder builderA = new ProcessBuilder("cmd.exe", "/c", "adb shell pm list packages");
            builderA.redirectErrorStream(true);
            Process processA = builderA.start();
            BufferedReader readerA = new BufferedReader(
                    new InputStreamReader(processA.getInputStream(), "GBK"));
            String lineA;
            List<CreateDevice> deviceList = new ArrayList<>();
            while ((lineA = readerA.readLine()) != null) {
                if (lineA.startsWith("package:com.facebook.lit")) {
                    String packageName = lineA.replace("package:", "").trim();

                    // 构建实体对象
                    CreateDevice device = new CreateDevice();
                    device.setDeviceName(deviceName);
                    device.setPackageName(packageName);
                    createDeviceService.insertCreateDevice(device);
                }
                if (lineA.startsWith("package:com.facebook.katan")) {
                    String packageName = lineA.replace("package:", "").trim();

                    // 构建实体对象
                    CreateDevice device = new CreateDevice();
                    device.setDeviceName(deviceName);
                    device.setPackageName(packageName);
                    createDeviceService.insertCreateDevice(device);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success();
    }

    @GetMapping("/openDevice/{keyIds}")
    @ResponseBody
    public AjaxResult openDevice(@PathVariable Long[] keyIds){

        for (Long keyId : keyIds) {
            CreateDevice createDevice = createDeviceService.selectCreateDeviceByKeyId(keyId);
            AppiumDriver appiumDriver = createDeviceService.openApp(createDevice);
            List<CreateInfo> createInfos = createInfoService.selectCreateInfoList(new CreateInfo());
            CreateInfo createInfo = null;

            for (CreateInfo info : createInfos) {
                if (!"已创建".equals(info.getCreateStatus())) {
                    createInfo = info;
                    break; // 找到就退出
                }
            }
            createDeviceService.CreateAccounnt(appiumDriver, createDevice, createInfo);
        }
        return success();
    }


    /**
     *
     * 修改备注
     */
    @GetMapping("/changeNote")
    @ResponseBody
    public AjaxResult changeNote(@RequestParam("id") List<Long> id, @RequestParam(value = "note", defaultValue = "") String note){

        List<CreateDevice> createDevices = createDeviceService.selectCreateDeviceByKeyIds(id.toArray(new Long[0]));
        for (CreateDevice createDevice : createDevices) {
            createDevice.setNote(note);
            createDeviceService.updateCreateDevice(createDevice);
        }
        return success();
    }
}
