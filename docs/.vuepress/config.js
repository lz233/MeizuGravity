module.exports = {
  lang: 'zh-CN',
  title: 'Meizu Gravity',
  head: [['link', { rel: 'icon', href: '/images/fav.png' }]],
  description: 'Let this screen do what it should do.',

  themeConfig: {
    logo: null,
    navbar: [
      {
        text: '应用',
        link: 'application.md',
      },
      {
        text: 'GitHub',
        link: 'https://github.com/lz233/MeizuGravity',
      },
    ],
    // 侧边栏对象
    // 不同子路径下的页面会使用不同的侧边栏
    sidebar: "auto",
  },
  markdown: {
      extractHeaders: {
          level: [1,2,3],
        },
},
  dest: 'public',
}