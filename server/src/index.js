const Koa = require('koa');
const app = new Koa();
const server = require('http').createServer(app.callback());
const WebSocket = require('ws');
const wss = new WebSocket.WebSocketServer({server});
const Router = require('koa-router');
const cors = require('koa-cors');
const bodyParser = require('koa-bodyparser');
const convert = require('koa-convert');
const fs = require('fs');

app.use(bodyParser());

app.use(cors());

app.use(convert(function* (next) {
    const start = new Date();
    yield Promise.all(next);
    const ms = new Date() - start;
    console.log(`${start} ${this.method} ${this.url} - ${ms}ms`);
}));

const broadcast = (data) =>
    wss.clients.forEach((client) => {
        client.on('open', function open() {
            client.send(JSON.stringify(data));
        })
    });


let products = JSON.parse(fs.readFileSync('./products.json'));
let warehouses = []

const router = new Router();
router.get('/products' , ctx => {
    const warehouseId = ctx.request.query['warehouseId']
    ctx.response.body = products.filter(obj => {
        return obj.warehouseId == warehouseId
    })
    ctx.response.status = 200;
});

router.post('/product', ctx => {
    const product = ctx.request.body;
    product.id = products.length + 1;
    products.push(product);
    fs.writeFileSync("./products.json", JSON.stringify(products, null, 2))
    ctx.response.body = product;
    ctx.response.status = 201;
    broadcast(product);
});

router.post('/products', ctx => {
    const sentProducts = ctx.request.body;
    sentProducts.forEach(product => {
        product.isUploaded = true
        product.id = products.length + 1;
        products.push(product)
    })
    fs.writeFileSync("./products.json", JSON.stringify(products, null, 2))
    ctx.response.body = sentProducts;
    ctx.response.status = 201;
    broadcast(sentProducts);
});

router.put('/product', ctx => {
    const newProduct = ctx.request.body;
    let index = products.findIndex(obj => {
        return obj.id === newProduct.id
    })
    products[index].name = newProduct.name
    products[index].brand = newProduct.brand
    products[index].price = newProduct.price
    products[index].isPerUnit = newProduct.isPerUnit
    products[index].expirationDate = newProduct.expirationDate
    products[index].isRefrigerated = newProduct.isRefrigerated
    products[index].image = newProduct.image
    fs.writeFileSync("./products.json", JSON.stringify(products, null, 2))
    ctx.response.body = newProduct;
    ctx.response.status = 200;
    broadcast(newProduct);
});

router.del('/products/:id', ctx => {
    let productId = ctx.params['id']
    let index = products.findIndex(obj => {
        return obj.id == productId
    })
    products.splice(index, 1);
    fs.writeFileSync("./products.json", JSON.stringify(products, null, 2))
    ctx.response.status = 200;
})

router.get('/warehouses', ctx => {
    let rawdata = fs.readFileSync('./warehouses.json');
    warehouses = JSON.parse(rawdata);
    ctx.response.body = warehouses
    ctx.response.status = 200;
});

app.use(router.routes());
app.use(router.allowedMethods());

server.listen(3000);