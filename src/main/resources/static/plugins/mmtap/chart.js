// API http://yapi.weilaigongzuo.com/project/267/interface/api/cat_919
// <script src="https://p1.topproio.com/highcharts.js"></script>
// <script src="https://p1.topproio.com/highchartsmore.js"></script>
// <script src="https://p1.topproio.com/networkgraph.js"></script>
// Vue 2.6

const request = url => fetch(window.location.origin + url).then(response => response.json())

// 年度专利图形
const AnnualPatents = Vue.component('annual_patents', {
  template: '<div id="annualPatents" style="height: 600px"></div>',
  mounted () {
    request('/patents/annual').then(res => {
      const xData = Object.keys(res.data)
      const yData = xData.map(item => {
        return res.data[item]
      })

      Highcharts.chart('annualPatents', {
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
    })
  }
})
// 专利权人和其拥有的专利数量
const PatenteeOwnedNumbers = Vue.component('patentee_owned_patents', {
  template: '<div id="patenteeOwnedNumbers" style="min-height: 800px"></div>',
  mounted () {
    request('/patents/patentee').then(res => {
      Highcharts.chart('patenteeOwnedNumbers', {
        chart: {
          type: 'packedbubble',
          height: '100%',
        },
        title: {
          text: '专利权人和其拥有的专利数量'
        },
        tooltip: {
          useHTML: true,
          pointFormat: '<b>{point.name}:</b> {point.y}'
        },
        plotOptions: {
          packedbubble: {
            dataLabels: {
              enabled: true,
              format: '{point.name}',
              filter: {
                property: 'y',
                operator: '>',
                value: 20
              },
              style: {
                color: 'black',
                textOutline: 'none',
                fontWeight: 'normal'
              }
            },
            minPointSize: 20
          }
        },
        series: res.data,
        responsive: {
          rules: [{
            condition: {
              maxWidth: 500
            },
            chartOptions: {
              legend: {
                align: 'right',
                verticalAlign: 'middle',
                layout: 'vertical'
              }
            }
          }]
        }
      })
    })
  }
})
// 主分类的数量比对
const CategoriesComparisonAnalysis = Vue.component('categories_comparison_analysis', {
  template: '<div id="mainCategoriesComparsion" style="min-height: 800px"></div>',
  mounted () {
    request('/patents/categories_comparsion').then(res => {
      Highcharts.chart('mainCategoriesComparsion', {
        chart: {
          type: 'packedbubble',
          height: '100%',
        },
        title: {
          text: '主分类的数量比对'
        },
        tooltip: {
          useHTML: true,
          pointFormat: '<b>{point.name}:</b> {point.y}'
        },
        plotOptions: {
          packedbubble: {
            dataLabels: {
              enabled: true,
              format: '{point.name}',
              filter: {
                property: 'y',
                operator: '>',
                value: 20
              },
              style: {
                color: 'black',
                textOutline: 'none',
                fontWeight: 'normal'
              }
            },
            minPointSize: 20
          }
        },
        series: res.data,
        responsive: {
          rules: [{
            condition: {
              maxWidth: 500
            },
            chartOptions: {
              legend: {
                align: 'right',
                verticalAlign: 'middle',
                layout: 'vertical'
              }
            }
          }]
        }
      })
    })
  }
})
// 多重共现网络可视化
const MultipleNetworkVisualization = Vue.component('multiple_network_visualization', {
  template: '<div id="multipleNetworkOccurrence"></div>',
  mounted () {
    request('/patents/network').then(res => {
      // 极限渲染数大概在 500 左右，需要做分页
      const data = res.data.slice(2800)
      Highcharts.chart('multipleNetworkOccurrence', {
        chart: {
          type: 'networkgraph',
          height: '100%'
        },
        title: {
          text: '多重共现网络可视化'
        },
        plotOptions: {
          networkgraph: {
            keys: ['from', 'to'],
            layoutAlgorithm: {
              enableSimulation: true
            }
          }
        },
        series: [{
          dataLabels: {
            enabled: true
          },
          data: data
        }]
      })
    })
  }
})