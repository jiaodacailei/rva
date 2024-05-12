## 开发

16.16.0
```bash
# 克隆项目
git clone https://github.com/jiaodacailei/rva-tag

# 安装依赖
npm install --legacy-peer-deps

# 强烈建议不要用直接使用 cnpm 安装，会有各种诡异的 bug，可以通过重新指定 registry 来解决 npm 安装速度慢的问题。
npm install --registry=https://registry.npm.taobao.org --legacy-peer-deps

# 启动服务
npm run dev

```

浏览器访问 http://localhost:80

## 发布

```bash
# 构建测试环境
npm run build:stage

# 构建生产环境
npm run build:prod
```