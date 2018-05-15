const gulp      = require('gulp');
const uglify    = require('gulp-uglify');
const uglifycss = require('gulp-uglifycss');
const concat    = require('gulp-concat');
const rename    = require('gulp-rename');
const clean     = require('gulp-clean');
const replace   = require('gulp-replace');
const babel     = require('gulp-babel');
const watch     = require('gulp-watch');

const options = {
  baseDir: "./src/main/resources/static",
  jsFiles: [
    "./src/main/resources/static/js/*.js"
  ],
  cssFiles: [
    "./src/main/resources/static/css/*.css"
  ],
  distFolder: "./src/main/resources/static/dist",
  fontsFolder: "./src/main/resources/static/dist/fonts",
  dependencies: {
    css: [
      "./node_modules/bootstrap/dist/css/bootstrap.min.css",
      "./node_modules/font-awesome/css/font-awesome.min.css",
      "./node_modules/metismenu/dist/metisMenu.min.css",
      "./node_modules/bootstrap-datepicker/dist/css/bootstrap-datepicker.min.css",
      "./node_modules/jquery-bootstrap-scrolling-tabs/dist/jquery.scrolling-tabs.min.css",
      "./node_modules/bootstrap-select/dist/css/bootstrap-select.min.css"
    ],
    js: [
      "./node_modules/jquery/dist/jquery.min.js",
      "./node_modules/bootstrap/dist/js/bootstrap.min.js",
      "./node_modules/metismenu/dist/metisMenu.min.js",
      "./node_modules/hideseek/jquery.hideseek.min.js",
      "./node_modules/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js",
      "./node_modules/bootstrap-datepicker/dist/locales/bootstrap-datepicker.pt-BR.min.js",
      "./node_modules/chart.js/dist/Chart.min.js",
      "./node_modules/chart.piecelabel.js/build/Chart.PieceLabel.min.js",
      "./node_modules/jquery-bootstrap-scrolling-tabs/dist/jquery.scrolling-tabs.min.js",
      "./node_modules/bootstrap-select/dist/js/bootstrap-select.min.js"
    ],
    fonts: [
      "./node_modules/font-awesome/fonts/*",
      "./node_modules/bootstrap/fonts/*"
    ]
  }
};

gulp.task('clean', _clean);

gulp.task('default', ['dist_fonts', 'dist_js', 'dist_css']);

gulp.task('dist_fonts', _dist_fonts);

gulp.task('dist_js', _dist_js);

gulp.task('dist_css', _dist_css);

gulp.task('watch', () => {
  return watch(options.cssFiles.concat(options.jsFiles), () => {
    _dist_js();
    _dist_css();
    console.log(new Date().toString().substring(16, 24) + " > done")
  });
})

function _clean() {
  gulp.src(options.distFolder)
    .pipe(clean());
}

function _dist_fonts() {
  gulp.src(options.dependencies.fonts)
    .pipe(gulp.dest(options.fontsFolder));
}

function _dist_js() {
  gulp.src(options.dependencies.js.concat(options.jsFiles))
    .pipe(concat(options.distFolder))
    .pipe(replace('../fonts', '../dist/fonts'))
    .pipe(rename('dist.min.js'))
    .pipe(uglify())
    .pipe(gulp.dest(options.distFolder));
}

function _dist_css() {
  gulp.src(options.dependencies.css.concat(options.cssFiles))
    .pipe(concat(options.distFolder))
    .pipe(replace('../fonts', '../dist/fonts'))
    .pipe(rename('dist.min.css'))
    .pipe(uglifycss())
    .pipe(gulp.dest(options.distFolder));
}
