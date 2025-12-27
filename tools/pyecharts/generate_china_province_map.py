from pathlib import Path

from pyecharts import options as opts
from pyecharts.charts import Map
from pyecharts.globals import CurrentConfig


def main() -> None:
    # 使用在线资源（Android WebView 需要联网才能加载 echarts 资源）
    CurrentConfig.ONLINE_HOST = "https://assets.pyecharts.org/assets/v5/"

    data = [
        ("北京", 10), ("天津", 8), ("上海", 12), ("重庆", 9),
        ("河北", 7), ("河南", 11), ("云南", 15), ("辽宁", 6),
        ("黑龙江", 5), ("湖南", 10), ("安徽", 7), ("山东", 9),
        ("新疆", 14), ("江苏", 10), ("浙江", 13), ("江西", 8),
        ("湖北", 9), ("广西", 12), ("甘肃", 4), ("山西", 5),
        ("内蒙古", 4), ("陕西", 6), ("吉林", 4), ("福建", 8),
        ("贵州", 9), ("广东", 11), ("青海", 3), ("西藏", 3),
        ("四川", 12), ("宁夏", 3), ("海南", 6), ("台湾", 7),
        ("香港", 2), ("澳门", 2),
    ]

    chart = (
        Map(init_opts=opts.InitOpts(width="100%", height="100%"))
        .add(series_name="省份", data_pair=data, maptype="china", is_map_symbol_show=False)
        .set_global_opts(
            tooltip_opts=opts.TooltipOpts(trigger="item", formatter="{b}：{c}"),
            visualmap_opts=opts.VisualMapOpts(
                min_=0,
                max_=15,
                is_calculable=True,
                range_color=["#E6F7FF", "#1890FF"],
            ),
        )
    )

    # 绑定点击事件：点击省份 -> 调 Android 接口（ProvinceMapActivity 里 addJavascriptInterface 的 name 叫 "Android"）
    chart_id = chart.chart_id
    chart.add_js_funcs(
        f"""
        var chart = chart_{chart_id};
        chart.on('click', function(params) {{
          try {{
            if (window.Android && typeof window.Android.onProvinceClick === 'function') {{
              window.Android.onProvinceClick(params.name || '');
            }} else {{
              alert('点击：' + (params.name || ''));
            }}
          }} catch(e) {{}}
        }});
        """
    )

    out = Path(__file__).resolve().parents[2] / "app" / "src" / "main" / "assets" / "china_province_map.html"
    out.parent.mkdir(parents=True, exist_ok=True)
    chart.render(str(out))
    print(f"generated: {out}")


if __name__ == "__main__":
    main()


