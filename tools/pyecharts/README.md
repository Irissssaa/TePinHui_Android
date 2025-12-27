## 说明
这个目录用于用 **Python + pyecharts** 生成 Android WebView 使用的中国省份可交互地图页面。

## 生成目标文件
- `app/src/main/assets/china_province_map.html`

## 使用方法
在仓库根目录执行（本机需要 Python 3）：

```bash
pip install -r TePinHui_Android/tools/pyecharts/requirements.txt
python TePinHui_Android/tools/pyecharts/generate_china_province_map.py
```

生成后打开 App：首页「特产故事」入口会进入地图页，点击省份会在 App 内 Toast 显示省份名称。


