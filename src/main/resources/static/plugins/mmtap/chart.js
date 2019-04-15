// API http://yapi.weilaigongzuo.com/project/267/interface/api/cat_919
// <script src="https://p1.topproio.com/highcharts.js"></script>
// <script src="https://p1.topproio.com/highchartsmore.js"></script>
// <script src="https://p1.topproio.com/networkgraph.js"></script>
// Vue 2.6
// <script>

const request = url => fetch(window.location.origin + url).then(response => response.json())
// 年度专利图形
const AnnualPatents = Vue.component('annual_patents', {
    template: '<div id="annualPatents" style="height: 600px"></div>',
    mounted () {
        request('/patents/annual').then(res => {
            this.render(res.data)
    })
    },
    methods: {
        render(source) {
            const xData = Object.keys(source)
            const yData = xData.map(item => {
                return source[item]
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
        }
    }
})
// 专利权人和其拥有的专利数量
const PatenteeOwnedNumbers = Vue.component('patentee_owned_patents', {
    template: '<div id="patenteeOwnedNumbers" style="min-height: 800px"></div>',
    mounted () {
        request('/patents/patentee').then(res => {
            this.render(res.data)
    })
    },
    methods: {
        render(source) {
            const colors = ['#7cb5ec', '#434348', '#90ed7d', '#7cb5ec']
            const patenteeNodes = source.map(record => {
                return {
                    id: record.name,
                    mass: 1,
                    marker: {
                        radius: record.value *10
                    },
                    dataLabels: {
                        // enabled: record.value < 30 ? false : true,
                        enabled: true,
                    },
                    color: colors[Math.floor(Math.random() * colors.length)]
                }
            })
            Highcharts.chart('patenteeOwnedNumbers', {
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
                            return 70
                        },
                        integration: 'euler',
                        // Half of the repulsive force
                        gravitationalConstant: 10
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
const AnalysisOfCategories = Vue.component('analysis_categories', {
    template: '<div id="analysisOfCategories">热门分类十年的申请数量分析</div>',
    mounted () {
        request('/patents/analysis_categories').then(res => {
            this.render(res.data)
    })
    },
    methods: {
        render(source) {
            const chart = Highcharts.chart('analysisOfCategories', {
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
const AnalysisOfCategoriesNetwork = Vue.component('analysis_categories_network', {
    template: `
        <div id="analysisOfCategoriesNetwork">热门分类十年的申请数量分析</div>
      `,
    mounted () {
        request('/patents/analysis_categories_network').then(res => {
            this.render(res.data)
    })
    },
    methods: {
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
            Highcharts.chart(analysisOfCategoriesNetwork, {
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
// 主分类的数量比对
const CategoriesComparisonAnalysis = Vue.component('categories_comparison_analysis', {
    template: `<div>
          <div id="mainCategoriesComparsion" style="min-height: 800px"></div>
          <analysis_categories></analysis_categories>
          <analysis_categories_network></analysis_categories_network>
        </div>`,
    mounted () {
        request('/patents/categories_comparsion').then(res => {
            this.render(res.data)
    })
    },
    methods: {
        render(source) {
            const colors = ['#7cb5ec', '#434348', '#90ed7d', '#7cb5ec']
            const recordNodes = source.map(record => {
                return {
                    id: record.name,
                    mass: 1,
                    marker: {
                        radius: record.value / 5
                        // radius: record.value / 3
                    },
                    dataLabels: {
                        // enabled: record.value < 100 ? false : true,
                        enabled: true,
                    },
                    color: colors[Math.floor(Math.random() * colors.length)]
                }
            })
            Highcharts.chart('mainCategoriesComparsion', {
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
// 多重共现网络可视化
const MultipleNetworkVisualization = Vue.component('multiple_network_visualization', {
        template: '<div id="multipleNetworkOccurrence"></div>',
        mounted () {
            request('/patents/network').then(res => {
                this.render(res.data)
        })
        },
        methods: {
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
                Highcharts.chart('multipleNetworkOccurrence', {
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