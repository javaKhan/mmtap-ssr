


// 1. 图表右上侧菜单本地化配置 2. 关闭版权信息水印。
// 在 Highcharts.chart 前引入，全局仅需引入一次。

Highcharts.setOptions({
    lang:{
        contextButtonTitle:"图表导出菜单",
        decimalPoint:".",
        downloadJPEG:"下载JPEG图片",
        downloadPDF:"下载PDF文件",
        downloadPNG:"下载PNG文件",
        downloadSVG:"下载SVG文件",
        drillUpText:"返回 {series.name}",
        loading:"加载中",
        months:["一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"],
        noData:"没有数据",
        numericSymbols: [ "千" , "兆" , "G" , "T" , "P" , "E"],
        printChart:"打印图表",
        resetZoom:"恢复缩放",
        resetZoomTitle:"恢复图表",
        shortMonths: [ "Jan" , "Feb" , "Mar" , "Apr" , "May" , "Jun" , "Jul" , "Aug" , "Sep" , "Oct" , "Nov" , "Dec"],
        thousandsSep:",",
        weekdays: ["星期一", "星期二", "星期三", "星期四", "星期五", "星期六","星期天"]
    },
    credits: {
        enabled: false // 禁用版权信息
    }
})
// 请求函数的简单封装，不建议直接使用
const request = url => fetch(window.location.origin + url).then(response => response.json())

// const app = new Vue({}).$mount('#app')

// 以下是所有的图形组件
// 年度专利图形，也可以作为北京地区或者全国地区的统计
const AnnualPatents = Vue.component('annual_patents', {
  template: '<div id="annualPatents" style="height: 600px"></div>',
  data () {
    return {
      chart: null
    }
  },
  methods: {
    exportJPEG(callback) {
      this.chart.exportChartLocal({
          type: 'image/jpeg',
          filename: 'chart',
          sourceHeight: 800,
          getImgBase64(data) {
            callback && callback(data)
          }
      })
    },
    render(source) {
      // const xData = Object.keys(source)
      // const yData = xData.map(item => {return source[item]})
      const xData = source.map(item => item[0])
      const yData = source.map(item => item[1])
      this.chart = Highcharts.chart('annualPatents', {
        chart: {
          type: 'spline'
        },
        title: {
          text: '年度专利数量'
        },
        xAxis: {
          categories: xData
        },
        yAxis: {
          title: {
            text: '专利数'
          },
          labels: {
            formatter: function () {
              return this.value
            }
          }
        },
        tooltip: {
          crosshairs: true,
          shared: true
        },
        plotOptions: {
          spline: {
            marker: {
              radius: 4,
              lineColor: '#666666',
              lineWidth: 1
            }
          }
        },
        series: [{
          name: '年度',
          marker: {
            symbol: 'square'
          },
          data: yData
        }]
      })
    }
  }
})
// 专利权人和其拥有的专利数量，也可以作为北京地区或者全国地区的统计
const PatenteeOwnedPatents = Vue.component('patentee_owned_patents', {
  template: '<div id="patenteeOwnedNumbers" style="min-height: 800px"></div>',
  data() {
    return {
      chart: ''
    }
  },
  methods: {
    exportJPEG(callback) {
      this.chart.exportChartLocal({
          type: 'image/jpeg',
          filename: 'chart',
          sourceHeight: 800,
          getImgBase64(data) {
            callback && callback(data)
          }
      })
    },
    render(source) {
      const colors = ['#7cb5ec', '#434348', '#90ed7d', '#7cb5ec']
      const patenteeNodes = source.map(record => {
        return {
          id: record.name,
          mass: 1,
          marker: {
            radius: record.value / 6
          },
          dataLabels: {
            enabled: record.value < 100 ? false : true,
          },
          color: colors[Math.floor(Math.random() * colors.length)]
        }
      })
      this.chart = Highcharts.chart('patenteeOwnedNumbers', {
        chart: {
          type: 'networkgraph'
        },
        title: {
          text: '专利权人和其拥有的专利数量'
        },
        plotOptions: {
          networkgraph: {
            turboThreshold: 0,
            keys: ['from', 'to', 'color']
          }
        },
        series: [{
          type: 'networkgraph',
          layoutAlgorithm: {
            enableSimulation: true,
            initialPositions: 'random',
            // Applied only to links, should be 0
            attractiveForce: function () {
              return 0
            },
            repulsiveForce: function () {
              return 80
            },
            integration: 'euler',
            // Half of the repulsive force
            gravitationalConstant: 20
          },
          nodes: patenteeNodes,
          // No links, only nodes:
          data: []
        }]
      })
    }
  }
})
// 热门分类十年的申请数量分析（直方图）
const AnalysisCategories = Vue.component('analysis_categories', {
  template: `
    <div id="analysisOfCategories">热门分类十年的申请数量分析</div>
  `,
  data() {
    return {
      chart: ''
    }
  },
  methods: {
    exportJPEG(callback) {
      this.chart.exportChartLocal({
          type: 'image/jpeg',
          filename: 'chart',
          sourceHeight: 800,
          getImgBase64(data) {
            callback && callback(data)
          }
      })
    },
    render(source) {
      this.chart = Highcharts.chart('analysisOfCategories', {
        chart: {
          type: 'column'
        },

        title: {
          text: '主分类号 / 公开日'
        },

        subtitle: {
          text: '热门分类十年的申请数量分析'
        },

        legend: {
          align: 'right',
          verticalAlign: 'middle',
          layout: 'vertical'
        },

        xAxis: {
          categories: source.categories,
          labels: {
            x: -10
          }
        },

        yAxis: {
          allowDecimals: false,
          title: {
            text: '记录数'
          }
        },

        series: source.series,

        responsive: {
          rules: [{
            condition: {
              maxWidth: 500
            },
            chartOptions: {
              legend: {
                align: 'center',
                verticalAlign: 'bottom',
                layout: 'horizontal'
              },
              yAxis: {
                labels: {
                  align: 'left',
                  x: 0,
                  y: -5
                },
                title: {
                  text: null
                }
              },
              subtitle: {
                text: null
              },
              credits: {
                enabled: false
              }
            }
          }]
        }
      })
    }
  }
})
// 热门分类十年的申请数量分析（网络图）
const AnalysisCategoriesNetwork = Vue.component('analysis_categories_network', {
  template: `
    <div id="analysisOfCategoriesNetwork">热门分类十年的申请数量分析</div>
  `,
  data() {
    return {
      chart: ''
    }
  },
  methods: {
    exportJPEG(callback) {
      this.chart.exportChartLocal({
          type: 'image/jpeg',
          filename: 'chart',
          sourceHeight: 800,
          getImgBase64(data) {
            callback && callback(data)
          }
      })
    },
    render(source) {
      // 按 ipc 所占记录数标红和大小
      const IPCs = source.reduce((acc, curr) => {
        if (!acc[curr[0]]) {
          acc[curr[0]] = 1
        } else {
          acc[curr[0]] ++
        }
        return acc
      }, {})
      const IPCsOptions = Object.keys(IPCs).map(ipc => {
        return {
          id: ipc,
          dataLabels: {
            enabled: true
          },
          marker: {
            radius: IPCs[ipc] / 5,
            fillColor: '#f15c80'
          }
        }
      })
      // 显示与所有 ipc 有关的记录的 label，当该记录数的 count >= IPCS.length 的时候就 break 循环
      const patentees = []
      const ipcsLength = Object.keys(IPCs).length
      for (let i = 0; i < source.length; i ++) {
        let count = 0
        for (let x = 0; x < source.length; x ++) {
          if (count >= ipcsLength) {
            patentees.push(source[i])
            count = 0
            break
          }
          if (source[i][1] === source[x][1] && source[i][0] !== source[x][0]) {
            count ++
          }
        }
      }
      const patenteesOptions = patentees.map(patentee => {
        return {
          id: patentee[1],
          dataLabels: {
            enabled: true
          },
          marker: {
            radius: 7,
            fillColor: '#90ed7d'
          }
        }
      })
      this.chart = Highcharts.chart(analysisOfCategoriesNetwork, {
        chart: {
          type: 'networkgraph',
          height: '100%'
        },
        title: {
          text: '多重共线网络可视化'
        },
        subtitle: {
          text: '热门分类十年的申请数量分析'
        },
        plotOptions: {
          networkgraph: {
            layoutAlgorithm: {
              enableSimulation: true,
              integration: 'euler',
              linkLength: 60
            },
            keys: ['from', 'to'],
            marker: {
              radius: 5,
              lineWidth: 1
            }
          }
        },
        series: [{
          nodes: [...IPCsOptions,...patenteesOptions],
          data: source
        }]
      })
    }
  }
})
// 主分类的数量比对，，也可以作为北京地区或者全国地区的统计
const CategoriesComparisonAnalysis = Vue.component('categories_comparison_analysis', {
  template: `
    <div id="mainCategoriesComparsion" style="min-height: 800px"></div>
  `,
  data() {
    return {
      chart: ''
    }
  },
  methods: {
    exportJPEG(callback) {
      this.chart.exportChartLocal({
          type: 'image/jpeg',
          filename: 'chart',
          sourceHeight: 800,
          getImgBase64(data) {
            callback && callback(data)
          }
      })
    },
    render(source) {
      const colors = ['#7cb5ec', '#434348', '#90ed7d', '#7cb5ec']
      const recordNodes = source.map(record => {
        return {
          id: record.name,
          mass: 1,
          marker: {
            radius: record.value / 20
          },
          dataLabels: {
            enabled: record.value < 100 ? false : true,
          },
          color: colors[Math.floor(Math.random() * colors.length)]
        }
      })
      this.chart = Highcharts.chart('mainCategoriesComparsion', {
        chart: {
          type: 'networkgraph'
        },
        title: {
          text: '主分类的数量比对'
        },
        plotOptions: {
          networkgraph: {
            turboThreshold: 0,
            keys: ['from', 'to', 'color']
          }
        },
        series: [{
          type: 'networkgraph',
          layoutAlgorithm: {
            enableSimulation: true,
            initialPositions: 'random',
            // Applied only to links, should be 0
            attractiveForce: function () {
              return 0
            },
            repulsiveForce: function () {
              return 80
            },
            integration: 'euler',
            // Half of the repulsive force
            gravitationalConstant: 20
          },
          nodes: recordNodes,
          // No links, only nodes:
          data: []
        }]
      })
    }
  }
})
// 多重共现网络可视化，，也可以作为北京地区或者全国地区的统计
const MultipleNetworkVisualization = Vue.component('multiple_network_visualization', {
  template: '<div id="multipleNetworkOccurrence"></div>',
  data () {
    return {
      chart: null
    }
  },
  methods: {
    exportJPEG(callback) {
      this.chart.exportChartLocal({
          type: 'image/jpeg',
          filename: 'chart',
          sourceHeight: 800,
          getImgBase64(data) {
            callback && callback(data)
          }
      })
    },
    render(source) {
      // 筛选记录数大于 5 的所有专利权人
      const multiplePatentees = source.reduce((acc, curr) => {
        if (!acc[curr[0]]) {
          acc[curr[0]] = 1
        } else {
          acc[curr[0]] ++
        }
        return acc
      }, {})
      const filterPatentees = Object.keys(multiplePatentees).filter(patentee => {
        return multiplePatentees[patentee] > 5
      }).map(patentee => {
        return {
          id: patentee,
          dataLabels: {
            enabled: true
          },
          marker: {
            radius: multiplePatentees[patentee] / 3,
            fillColor: '#f15c80'
          }
        }
      })
      this.chart = Highcharts.chart('multipleNetworkOccurrence', {
        chart: {
          type: 'networkgraph',
          height: '70%',
          marginBottom: 100,
          verticalAlign: 'top',
          align: 'middle'
        },
        title: {
          text: '多重共现网络可视化'
        },
        plotOptions: {
          networkgraph: {
            keys: ['from', 'to'],
            layoutAlgorithm: {
              enableSimulation: true,
              integration: 'verlet',
              linkLength: 60
            },
            dataLabels: {
              enabled: false
            },
          }
        },
        series: [{
          nodes: [...filterPatentees],
          data: source
        }]
      })
    }
  }
})
