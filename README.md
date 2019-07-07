# flipGallery
![FlipGallery](./resources/adv.png)



### 使用

```groovy
TODO 正在上传到 jcenter
```



### 示例代码:

```Kotlin
// resIds
        var flipGallery = findViewById<FlipGallery>(R.id.flip_gallery)
        flipGallery.setResIds(arrayOf(
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
        flipGallery.setUrls(arrayOf(
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



