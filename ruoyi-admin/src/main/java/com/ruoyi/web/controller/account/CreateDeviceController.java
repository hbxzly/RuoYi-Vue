package com.ruoyi.web.controller.account;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.account.domain.CreateInfo;
import com.ruoyi.account.domain.FbAccountForSell;
import com.ruoyi.account.service.ICreateInfoService;
import com.ruoyi.account.service.IFbAccountForSellService;
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

    @Autowired
    private IFbAccountForSellService fbAccountForSellService;

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
            List<String> deviceNames = selectOnlineDeviceNames();

            if (deviceNames.isEmpty()) {
                return error("未找到任何在线设备");
            }

            Set<String> existingDevices = new HashSet<>();
            List<CreateDevice> allDevices = createDeviceService.selectCreateDeviceList(new CreateDevice());
            for (CreateDevice device : allDevices) {
                existingDevices.add(buildDeviceUniqueKey(device.getDeviceName(), device.getPackageName()));
            }

            int addCount = 0;
            int skipCount = 0;
            for (String deviceName : deviceNames) {
                System.out.println("获取到设备名: " + deviceName);

                String deviceVersion = null;
                ProcessBuilder versionBuilder = new ProcessBuilder("cmd.exe", "/c", "adb -s " + deviceName + " shell getprop ro.build.version.release");
                versionBuilder.redirectErrorStream(true);
                Process versionProcess = versionBuilder.start();
                BufferedReader versionReader = new BufferedReader(
                        new InputStreamReader(versionProcess.getInputStream(), "GBK"));
                String versionLine = versionReader.readLine();
                if (versionLine != null) {
                    deviceVersion = versionLine.trim();
                }
                versionProcess.waitFor();

                ProcessBuilder builderA = new ProcessBuilder("cmd.exe", "/c", "adb -s " + deviceName + " shell pm list packages");
                builderA.redirectErrorStream(true);
                Process processA = builderA.start();
                BufferedReader readerA = new BufferedReader(
                        new InputStreamReader(processA.getInputStream(), "GBK"));
                String lineA;
                while ((lineA = readerA.readLine()) != null) {
                    if (lineA.startsWith("package:com.facebook.lit") || lineA.startsWith("package:com.facebook.katan")) {
                        String packageName = lineA.replace("package:", "").trim();
                        String uniqueKey = buildDeviceUniqueKey(deviceName, packageName);
                        if (existingDevices.contains(uniqueKey)) {
                            skipCount++;
                            continue;
                        }

                        CreateDevice device = new CreateDevice();
                        device.setDeviceName(deviceName);
                        device.setDeviceVersion(deviceVersion);
                        device.setPackageName(packageName);
                        createDeviceService.insertCreateDevice(device);
                        existingDevices.add(uniqueKey);
                        addCount++;
                    }
                }
                processA.waitFor();
            }
            return success("获取完成，新增" + addCount + "个，跳过已存在" + skipCount + "个");
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
    }

    @GetMapping("/onlineDevices")
    @ResponseBody
    public AjaxResult onlineDevices(){
        try {
            List<String> deviceNames = selectOnlineDeviceNames();
            if (deviceNames.isEmpty()) {
                return error("未找到任何在线设备");
            }
            return success(deviceNames);
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
    }

    private List<String> selectOnlineDeviceNames() throws Exception {
        List<String> deviceNames = new ArrayList<>();
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "adb devices");
        builder.redirectErrorStream(true);
        Process process = builder.start();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), "GBK"));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.endsWith("\tdevice")) {
                deviceNames.add(line.split("\t")[0].trim());
            }
        }
        process.waitFor();
        return deviceNames;
    }

    private String buildDeviceUniqueKey(String deviceName, String packageName) {
        return (deviceName == null ? "" : deviceName) + "|" + (packageName == null ? "" : packageName);
    }

    @PostMapping("/openDevice")
    @ResponseBody
    public AjaxResult openDevice(@RequestBody CreateDevice createDevice){
        AppiumDriver appiumDriver = createDeviceService.openApp(createDevice);
        if (!isBlank(createDevice.getCreateAccountId())) {
            FbAccountForSell fbAccountForSell = fbAccountForSellService.selectFbAccountForSellById(createDevice.getCreateAccountId());
            if (fbAccountForSell == null) {
                return error("未找到账号ID：" + createDevice.getCreateAccountId());
            }
            createDeviceService.loginAccount(appiumDriver, fbAccountForSell);
            return success();
        }
        return success();
    }

    private boolean isBlank(String value) {
        return value == null || "".equals(value.trim());
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
