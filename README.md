
![FlipGallery](./resources/adv.png)


[ ![Download](https://api.bintray.com/packages/chinalwb/flipgallery/flipgallery/images/download.svg?version=1.0) ](https://bintray.com/chinalwb/flipgallery/flipgallery/1.0/link)

### 使用

```groovy
implementation 'com.github.chinalwb:flipgallery:1.0' 

jcenter 正在审核
```



### 示例代码:

```Kotlin
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
