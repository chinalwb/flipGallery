
![FlipGallery](./resources/adv.png)

[ ![Download](https://api.bintray.com/packages/chinalwb/flipgallery/flipgallery/images/download.svg) ](https://bintray.com/chinalwb/flipgallery/flipgallery/_latestVersion)



### 简单介绍一下

这是一个简单的画廊实现. 受到 Flipboard App 的样式启发,总想做一个类似的效果试试. 目前实现原理特别简单, 一个自定义 view 加上内部维护的三个 bitmap 对象, 加上手势控制(ACTION_MOVE 和 fling — VelocityTracker)以及 camera.rotateX (横向翻转效果也很容易, 如果有需要我可以做一下 你也可以 pull request 哦). 翻上一页的时候把当前图片的上半部分往下翻转, 同时把上一页的下半部分(默认 180°的翻转)往回翻转同样的角度; 同理翻下一页的时候, 把当前图片的下半部分往上翻转以及下一页图片的上半部分(默认 -180°的翻转)往回翻转同样的角度. 松开的时候根据 velocityTracker 的 yVelocity, 用这个数值跟 viewConfiguration.scaledMininumFlingVelocity 比较, 如果大于临界值则执行翻转的属性动画, 否则执行复原的属性动画, 同时如果速度不够则判断旋转角度是否大于 90 度. (以上所有知识点来自 HencoderPlus)

- 目前支持resId 以及 Glide 两种设置数据源的方式. 内部 LruCache 默认维护 10 个最近使用的 bitmap
- `FlipGalleryListener`提供`onReachTop` / `onReachEnd` 两个回调
- `setFlipDuration(duration: Long)` 设置翻页动画和复原动画的完成时间
- `setFlipIndex(index: Int)` 设置默认显示第几张图片
- `smoothFlipToIndex(index: Int, duration: Long)` 动画的方式在指定时间段内从当前显示的图片滑动到指定的图片
- `smoothFliptToStart` / `smoothFlipToEnd` 是相应的两个快捷方法
- 三个自定义 view 属性: `flipTextSize` / `flipReachTopText` / `flipReachEndText`



### 使用

```groovy
implementation 'com.github.chinalwb:flipgallery:1.0' 

jcenter 正在审核
```



### 示例代码:

```Kotlin
// XML 中
<com.chinalwb.flipgallerylib.FlipGallery
            android:id="@+id/flip_gallery"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            app:flipTextSize="20sp"
            app:flipReachTopText="This is the first page"
            app:flipReachEndText="终于让你发现了我的底线"
    />

// resIds
        var flipGallery = findViewById<FlipGallery>(R.id.flip_gallery)
        flipGallery.withResIds(arrayOf(
            R.drawable.a,
            R.drawable.b,
            R.drawable.c,
            R.drawable.d,
            R.drawable.e,
            R.drawable.x,
            R.drawable.y,
            R.drawable.z
        )).setFlipDuration(300).setFlipIndex(0)

// Glide
        flipGallery.withUrls(arrayOf(
            "http://ww1.sinaimg.cn/large/0065oQSqly1g2pquqlp0nj30n00yiq8u.jpg",
            "https://ww1.sinaimg.cn/large/0065oQSqly1g2hekfwnd7j30sg0x4djy.jpg",
            "https://ws1.sinaimg.cn/large/0065oQSqly1g0ajj4h6ndj30sg11xdmj.jpg",
            "https://ws1.sinaimg.cn/large/0065oQSqly1fytdr77urlj30sg10najf.jpg",
            "https://ws1.sinaimg.cn/large/0065oQSqly1fymj13tnjmj30r60zf79k.jpg",
            "https://ws1.sinaimg.cn/large/0065oQSqgy1fy58bi1wlgj30sg10hguu.jpg",
            "https://ws1.sinaimg.cn/large/0065oQSqgy1fxno2dvxusj30sf10nqcm.jpg"
        )).setFlipDuration(300).setFlipIndex(3)

// 自动转
	flipGallery.postDelayed({
            flipGallery.smoothFlipToIndex(0, 7000)
        }, 12000)
```





### 可以这样玩 resIds

![resIds](./resources/resIds.gif)



### 也可以这样玩 glide

![glide](./resources/glide.gif)



### 还可以手动操作

![manually](./resources/manually.gif)

--------

MIT License

Copyright (c) 2019 Rain Liu

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
