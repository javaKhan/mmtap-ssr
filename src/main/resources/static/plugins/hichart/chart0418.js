


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
        weekdays: ["星期一", "星期二", "星期三", "星期四", "星期五", "星期六","星期天"],
        viewFullscreen: '放大到全屏'
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
          sourceWidth: 600,
          sourceHeight: 600,
          getImgBase64(data) {
            callback && callback(data)
          }
      })
    },
    render(source) {
      /**
       * 0~2 深蓝，半径为 50 40 35
       * 3~5 普通蓝，半径为 30 28 25
       * > 5 浅蓝
       * 5~ 分成 10 份，0: 20, 1~4: 18 15 12 10, 5~9: 5
       * 0~5 标注名称
       */

       // 去掉 ;
       source.forEach(item => {
        item.name = item.name.replace(';', '')
       })

      // 降序排 value 值
      const descSource = source.sort((a, b) => {
        if (a.value < b.value) return 1
        if (a.value > b.value) return -1
        return 0
      })
      const radiusArray = [50, 40, 35, 30, 28, 25]

      /**
       * 将第 6 份以后的值分为 10 份，每一份填充既定的 size
       */
      const increasing = Math.floor((descSource.length - 6) / 10)
      let pre = 6
      const sizeMap = [20, 18, 15, 12, 10, 5, 5, 5, 5, 5]
      const sizeSourceMap = []
      sizeSourceMap.length = descSource.length
      for(let i = 0; i < 10; i ++) {
        sizeSourceMap.fill(sizeMap[i], pre, pre + increasing)
        pre = pre + increasing
      }
      const markSource = descSource.map((record, key) => {
        // 标注半径
        record['size'] = sizeSourceMap[key]

        if (key <= 5) {
          record['size'] = radiusArray[key]
        }

        // 标注颜色
        if (key <= 2) {
          record['color'] = '#195B9E'
        } else if (key > 2 && key <= 5) {
          record['color'] = '#488CBC'
        } else {
          record['color'] = '#69BAD9'
        }

        // 标注名称
        if (key <= 5) {
          record['label'] = true
        } else {
          record['label'] = false
        }
        return record
      })

      const patenteeNodes = markSource.map(record => {
        return {
          id: record.name,
          mass: 1,
          marker: {
            radius: record.size
          },
          dataLabels: {
            enabled: record.label
          },
          color: record.color,
          name: `${record.name}: <strong>${record.value}</strong>`
        }
      })
      this.chart = Highcharts.chart('patenteeOwnedNumbers', {
        chart: {
          type: 'networkgraph'
        },
        title: {
          text: '专利权人和其拥有的专利数量'
        },
        series: [{
          type: 'networkgraph',
          layoutAlgorithm: {
            enableSimulation: false,
            initialPositions: 'random',
            // 关联节点的排斥力，需要返回 0
            attractiveForce: function () {
              return 0
            },
            repulsiveForce: function () {
              return 180
            },
            integration: 'euler',
            // Half of the repulsive force
            gravitationalConstant: 20
          },
          nodes: patenteeNodes,
          // No links, only nodes:
          data: []
        }],
        exporting: {
          sourceWidth: 600,
          sourceHeight: 600,
          chartOptions: {
            subtitle: null
          }
        }
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
          sourceWidth: 600,
          sourceHeight: 600,
          getImgBase64(data) {
            callback && callback(data)
          }
      })
    },
    render(source) {
      /**
       * 1,2,3 个实心正方形: 20,15,13
       * 4,5,6,7,8,9,10 实心圆形：10, 8, 6, 4, 1
       */

      // 删除 ;
      source.forEach(item => {
        item[1] = item[1].replace(';', '')
      })

      const multipleIPCs = source.reduce((acc, curr) => {
        if (!acc[curr[0]]) {
          acc[curr[0]] = 1
        } else {
          acc[curr[0]] ++
        }
        return acc
      }, {})

      const descIPCs = Object.keys(multipleIPCs).map(ipc => {
        return {
          name: ipc,
          value: multipleIPCs[ipc]
        }
      }).sort((a, b) => {
        if (a.value > b.value) return -1
        if (a.value < b.value) return 1
        return 0
      })

      const radiusMap = []
      radiusMap.length = descIPCs.length
      radiusMap[0] = 20
      radiusMap[1] = 15
      radiusMap[2] = 10
      const increasing = Math.floor((descIPCs.length - 3) / 6)
      let pre = 3
      const sizeMap = [8, 5, 3, 2, 1, 1, 1]
      for(let i = 0; i < 7; i ++) {
        radiusMap.fill(sizeMap[i], pre, pre + increasing)
        pre = pre + increasing
      }

      const markIPCs = descIPCs.map((ipc, key) => {
        return {
          id: ipc.name,
          dataLabels: {
            enabled: true
          },
          marker: {
            radius: radiusMap[key],
            fillColor: '#f15c80',
            symbol: key < 2 ? 'square' : 'circle'
          },
          name: `${ipc.name}: <strong>${ipc.value}</strong>`
        }
      })

      // 显示与所有 ipc 有关的记录的 label，当该记录数的 count >= IPCS.length 的时候就 break 循环
      const patentees = []
      const ipcsLength = Object.keys(multipleIPCs).length
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

      this.chart = Highcharts.chart('analysisOfCategoriesNetwork', {
        chart: {
          type: 'networkgraph',
          height: '100%',
          marginBottom: 100,
          verticalAlign: 'top',
          align: 'middle'
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
              enableSimulation: false,
              integration: 'euler',
              linkLength: 40
            },
            keys: ['from', 'to'],
            marker: {
              radius: 5,
              lineWidth: 1
            }
          }
        },
        series: [{
          nodes: [...markIPCs,...patenteesOptions],
          data: source
        }],
        exporting: {
          sourceWidth: 600,
          sourceHeight: 600,
          chartOptions: {
            subtitle: null
          }
        }
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
          sourceHeight: 600,
          sourceWidth: 600,
          getImgBase64(data) {
            callback && callback(data)
          }
      })
    },
    render(source) {
      /**
       * 0~2 深蓝，半径为 50 40 35
       * 3~5 普通蓝，半径为 30 28 25
       * > 5 浅蓝
       * 5~ 分成 10 份，0: 20, 1~4: 18 15 12 10, 5~9: 5
       * 0~5 标注名称
       */

      // 降序排 value 值
      const descSource = source.sort((a, b) => {
        if (a.value < b.value) return 1
        if (a.value > b.value) return -1
        return 0
      })
      const radiusArray = [50, 40, 35, 30, 28, 25]

      /**
       * 将第 6 份以后的值分为 10 份，每一份填充既定的 size
       */
      const increasing = Math.floor((descSource.length - 6) / 10)
      let pre = 6
      const sizeMap = [20, 18, 15, 12, 10, 5, 5, 5, 5, 5]
      const sizeSourceMap = []
      sizeSourceMap.length = descSource.length
      for(let i = 0; i < 10; i ++) {
        sizeSourceMap.fill(sizeMap[i], pre, pre + increasing)
        pre = pre + increasing
      }
      const markSource = descSource.map((record, key) => {
        // 标注半径
        record['size'] = sizeSourceMap[key]

        if (key <= 5) {
          record['size'] = radiusArray[key]
        }

        // 标注颜色
        // 标注颜色
        if (key <= 2) {
          record['color'] = '#195B9E'
        } else if (key > 2 && key <= 5) {
          record['color'] = '#488CBC'
        } else {
          record['color'] = '#69BAD9'
        }

        // 标注名称
        if (key <= 5) {
          record['label'] = true
        } else {
          record['label'] = false
        }
        return record
      })

      const recordNodes = markSource.map(record => {
        return {
          id: record.name,
          mass: 1,
          marker: {
            radius: record.size
          },
          dataLabels: {
            enabled: record.label
          },
          color: record.color,
          name: `${record.name}: <strong>${record.value}</strong>`
        }
      })
      this.chart = Highcharts.chart('mainCategoriesComparsion', {
        chart: {
          type: 'networkgraph'
        },
        title: {
          text: '主分类的数量比对'
        },
        series: [{
          type: 'networkgraph',
          layoutAlgorithm: {
            enableSimulation: false,
            initialPositions: 'random',
            // Applied only to links, should be 0
            attractiveForce: function () {
              return 0
            },
            repulsiveForce: function () {
              return 120
            },
            integration: 'euler',
            // Half of the repulsive force
            gravitationalConstant: 20
          },
          nodes: recordNodes,
          // No links, only nodes:
          data: []
        }],
        exporting: {
          sourceWidth: 600,
          sourceHeight: 600,
          chartOptions: {
            subtitle: null
          }
        }
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
          sourceWidth: 600,
          sourceHeight: 600,
          getImgBase64(data) {
            callback && callback(data)
          }
      })
    },
    render(source) {

      // 删除 ';' 号
      source.forEach(item => {
        item[0] = item[0].replace(';', '')
      })

      const sourceSpread = [];
      for (let i = 0; i < source.length; i ++) {
        const patent = source[i][0]
        const ipc = source[i][1].split(';')
        sourceSpread.push([patent, ipc[0]])
        ipc.shift()
        if (ipc.length > 0) {
          ipc.forEach(item => {
            sourceSpread.push([patent, item])
          })
        }
      }

      function filterPatentHandler(limit) {
        const filterPatents = []
        for (let i = 0; i < sourceSpread.length; i ++) {
          let count = 0
          for (let x = 0; x < sourceSpread.length; x ++) {
            if (count >= limit) {
              count = 0
              filterPatents.push(sourceSpread[i])
              break
            }
            if (sourceSpread[i][0] === sourceSpread[x][0] && sourceSpread[i][1] !== sourceSpread[x][1]) {
              count ++
            }
          }
        }
        if (filterPatents.length > 150) {
          return filterPatentHandler(limit + 1)
        }
        return filterPatents
      }
      // 过滤出现次数为 3 的值，如果数量大于 150 则 3++ 直到满足条件
      const limitFilterPatents = filterPatentHandler(3);

      // 对出现次数的 patent 进行统计
      const multiplePantents = limitFilterPatents.reduce((acc, curr) => {
        if (!acc[curr[0]]) {
          acc[curr[0]] = 1
        } else {
          acc[curr[0]] ++
        }
        return acc
      }, {})

      const descPantents = Object.keys(multiplePantents).map(patentee => {
        return {
          name: patentee,
          value: multiplePantents[patentee]
        }
      }).sort((a, b) => {
        if (a.value > b.value) return -1
        if (a.value < b.value) return 1
        return 0
      })

      /**
       * 1,2,3 个实心正方形: 20,15,13
       * 4,5,6,7,8,9,10 实心圆形：10, 8, 6, 4, 1
       */
      const radiusMap = []
      radiusMap.length = descPantents.length
      radiusMap[0] = 20
      radiusMap[1] = 15
      radiusMap[2] = 10
      const increasing = Math.floor((descPantents.length - 3) / 6)
      let pre = 3
      const sizeMap = [8, 5, 3, 2, 1, 1, 1]
      for(let i = 0; i < 7; i ++) {
        radiusMap.fill(sizeMap[i], pre, pre + increasing)
        pre = pre + increasing
      }

      const limitDisplayPatentValue = descPantents[4].value

      const markPantents = descPantents.map((patent, key) => {
        return {
          id: patent.name,
          dataLabels: {
            enabled: patent.value >= limitDisplayPatentValue
          },
          marker: {
            radius: radiusMap[key],
            fillColor: '#f15c80',
            symbol: key < 2 ? 'square' : 'circle'
          },
          name: `${patent.name}: <strong>${patent.value}</strong>`
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
              enableSimulation: false,
              integration: 'verlet',
              linkLength: 60
            },
            dataLabels: {
              enabled: false
            },
          }
        },
        series: [{
          nodes: [...markPantents],
          data: limitFilterPatents
        }],
        exporting: {
          sourceWidth: 600,
          sourceHeight: 600,
          chartOptions: {
            subtitle: null
          }
        }
      })
    }
  }
})
