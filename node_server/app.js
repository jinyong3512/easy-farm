var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');
////////////////////////////////////////////////////////////////////////////////////////////////////////////////



var multer = require('multer');

var storage = multer.diskStorage({
  destination: function (req, file, cb) {
    cb(null, './uploads')
  },
  filename: function (req, file, cb) {
    cb(null, file.originalname)
  }
})

const upload = multer({ storage: storage })



////////////////////////////////////////////////////////////////////////////////////////////////////////////////
var indexRouter = require('./routes/index');
var usersRouter = require('./routes/users');
const { resourceLimits } = require('worker_threads');
const { loadavg } = require('os');

var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');


app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);
app.use('/users', usersRouter);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////




app.post('/PostUser', (req, res) => {
  console.log('');

  console.log('deviceId : ' + req.body.deviceId);     // String
  console.log('latitude : ' + req.body.latitude);     // Double
  console.log('longitude : ' + req.body.longitude);   // Double


  res.send('OK');
});

app.post('/alarm', (req, res) => {
  console.log('')

  console.log('deviceId : ' + req.body.deviceId);     // String

  res.send('0');
  // res.send('1');
});

app.post('/PostResult', upload.single('image'), (req, res) => {
  console.log('')

  console.log(req.file);               // imageFile

  console.log('deviceId : ' + req.body.deviceId);     // String
  console.log('pestName : ' + req.body.pestName);     // String
  console.log('pestPercentage : ' + req.body.pestPercentage);     // Double
  console.log('date : ' + req.body.date);     // String

  res.send('OK')
});

app.post('/GetResult', (req, res) => {
  console.log('')

  console.log('deviceId : ' + req.body.deviceId);   // String

  results = [{
    MyPlant_User_id: 'b68c54f2bfdf86c3',
    MyPlant_image_URL: 'https://search.pstatic.net/sunny/?src=http%3A%2F%2Fimg.theqoo.net%2Fimg%2FRBlCv.jpg&type=sc960_832',
    MyPlant_Pest: '콩불마름병',
    MyPlant_Date: '1111-11-11',
    MyPlant_Percentage: 11.11
  },
  {
    MyPlant_User_id: 'b68c54f2bfdf86c3',
    MyPlant_image_URL: 'https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMTA5MDNfMTYg%2FMDAxNjMwNjUwOTQzNTI4.F6uqmcr8f-F3HmkB-wpFu6m9ALPatmvdLt8fbHVYwywg.vT89QsWuSV-_4pw0T686S3ylTGD8Q3Feh2XRPji_TI0g.JPEG.ddot73%2FKakaoTalk_20210903_153355272.jpg&type=a340',
    MyPlant_Pest: '콩점무늬병',
    MyPlant_Date: '2222-22-22',
    MyPlant_Percentage: 22.22
  }
    ,
  {
    MyPlant_User_id: 'b68c54f2bfdf86c3',
    MyPlant_image_URL: 'https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMTA5MDNfMTYg%2FMDAxNjMwNjUwOTQzNTI4.F6uqmcr8f-F3HmkB-wpFu6m9ALPatmvdLt8fbHVYwywg.vT89QsWuSV-_4pw0T686S3ylTGD8Q3Feh2XRPji_TI0g.JPEG.ddot73%2FKakaoTalk_20210903_153355272.jpg&type=a340',
    MyPlant_Pest: '콩점무늬병',
    MyPlant_Date: '3333-33-33',
    MyPlant_Percentage: 33.33
  }
    ,
  {
    MyPlant_User_id: 'b68c54f2bfdf86c3',
    MyPlant_image_URL: 'https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMTA5MDNfMTYg%2FMDAxNjMwNjUwOTQzNTI4.F6uqmcr8f-F3HmkB-wpFu6m9ALPatmvdLt8fbHVYwywg.vT89QsWuSV-_4pw0T686S3ylTGD8Q3Feh2XRPji_TI0g.JPEG.ddot73%2FKakaoTalk_20210903_153355272.jpg&type=a340',
    MyPlant_Pest: '콩점무늬병',
    MyPlant_Date: '3333-33-33',
    MyPlant_Percentage: 33.33
  }
    ,
  {
    MyPlant_User_id: 'b68c54f2bfdf86c3',
    MyPlant_image_URL: 'https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMTA5MDNfMTYg%2FMDAxNjMwNjUwOTQzNTI4.F6uqmcr8f-F3HmkB-wpFu6m9ALPatmvdLt8fbHVYwywg.vT89QsWuSV-_4pw0T686S3ylTGD8Q3Feh2XRPji_TI0g.JPEG.ddot73%2FKakaoTalk_20210903_153355272.jpg&type=a340',
    MyPlant_Pest: '콩점무늬병',
    MyPlant_Date: '3333-33-33',
    MyPlant_Percentage: 33.33
  }
    ,
  {
    MyPlant_User_id: 'b68c54f2bfdf86c3',
    MyPlant_image_URL: 'https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMTA5MDNfMTYg%2FMDAxNjMwNjUwOTQzNTI4.F6uqmcr8f-F3HmkB-wpFu6m9ALPatmvdLt8fbHVYwywg.vT89QsWuSV-_4pw0T686S3ylTGD8Q3Feh2XRPji_TI0g.JPEG.ddot73%2FKakaoTalk_20210903_153355272.jpg&type=a340',
    MyPlant_Pest: '콩점무늬병',
    MyPlant_Date: '3333-33-33',
    MyPlant_Percentage: 33.33
  }
    ,
  {
    MyPlant_User_id: 'b68c54f2bfdf86c3',
    MyPlant_image_URL: 'https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMTA5MDNfMTYg%2FMDAxNjMwNjUwOTQzNTI4.F6uqmcr8f-F3HmkB-wpFu6m9ALPatmvdLt8fbHVYwywg.vT89QsWuSV-_4pw0T686S3ylTGD8Q3Feh2XRPji_TI0g.JPEG.ddot73%2FKakaoTalk_20210903_153355272.jpg&type=a340',
    MyPlant_Pest: '콩점무늬병',
    MyPlant_Date: '3333-33-33',
    MyPlant_Percentage: 33.33
  }
    ,
  {
    MyPlant_User_id: 'b68c54f2bfdf86c3',
    MyPlant_image_URL: 'https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMTA5MDNfMTYg%2FMDAxNjMwNjUwOTQzNTI4.F6uqmcr8f-F3HmkB-wpFu6m9ALPatmvdLt8fbHVYwywg.vT89QsWuSV-_4pw0T686S3ylTGD8Q3Feh2XRPji_TI0g.JPEG.ddot73%2FKakaoTalk_20210903_153355272.jpg&type=a340',
    MyPlant_Pest: '콩점무늬병',
    MyPlant_Date: '3333-33-33',
    MyPlant_Percentage: 33.33
  }
  ,
  {
    MyPlant_User_id: 'b68c54f2bfdf86c3',
    MyPlant_image_URL: 'https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMTA5MDNfMTYg%2FMDAxNjMwNjUwOTQzNTI4.F6uqmcr8f-F3HmkB-wpFu6m9ALPatmvdLt8fbHVYwywg.vT89QsWuSV-_4pw0T686S3ylTGD8Q3Feh2XRPji_TI0g.JPEG.ddot73%2FKakaoTalk_20210903_153355272.jpg&type=a340',
    MyPlant_Pest: '콩점무늬병',
    MyPlant_Date: '3333-33-33',
    MyPlant_Percentage: 33.33
  }
  ,
  {
    MyPlant_User_id: 'b68c54f2bfdf86c3',
    MyPlant_image_URL: 'https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMTA5MDNfMTYg%2FMDAxNjMwNjUwOTQzNTI4.F6uqmcr8f-F3HmkB-wpFu6m9ALPatmvdLt8fbHVYwywg.vT89QsWuSV-_4pw0T686S3ylTGD8Q3Feh2XRPji_TI0g.JPEG.ddot73%2FKakaoTalk_20210903_153355272.jpg&type=a340',
    MyPlant_Pest: '콩점무늬병',
    MyPlant_Date: '3333-33-33',
    MyPlant_Percentage: 33.33
  }
  ,
  {
    MyPlant_User_id: 'b68c54f2bfdf86c3',
    MyPlant_image_URL: 'https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMTA5MDNfMTYg%2FMDAxNjMwNjUwOTQzNTI4.F6uqmcr8f-F3HmkB-wpFu6m9ALPatmvdLt8fbHVYwywg.vT89QsWuSV-_4pw0T686S3ylTGD8Q3Feh2XRPji_TI0g.JPEG.ddot73%2FKakaoTalk_20210903_153355272.jpg&type=a340',
    MyPlant_Pest: '콩점무늬병',
    MyPlant_Date: '3333-33-33',
    MyPlant_Percentage: 33.33
  }
  ,
  {
    MyPlant_User_id: 'b68c54f2bfdf86c3',
    MyPlant_image_URL: 'https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMTA5MDNfMTYg%2FMDAxNjMwNjUwOTQzNTI4.F6uqmcr8f-F3HmkB-wpFu6m9ALPatmvdLt8fbHVYwywg.vT89QsWuSV-_4pw0T686S3ylTGD8Q3Feh2XRPji_TI0g.JPEG.ddot73%2FKakaoTalk_20210903_153355272.jpg&type=a340',
    MyPlant_Pest: '콩점무늬병',
    MyPlant_Date: '3333-33-33',
    MyPlant_Percentage: 33.33
  }]

  // console.log(results)

  resultsJSON = JSON.stringify(results)

  // console.log(resultsJSON)

  res.send(resultsJSON)
});

app.post('/deleteResult', (req, res) => {
  console.log('')

  console.log('imgUrl : ' + req.body.imgUrl);     // String

  res.send('OK');
});




////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// catch 404 and forward to error handler
app.use(function (req, res, next) {
  next(createError(404));
});

// error handler
app.use(function (err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;