# Attrs、Style、Theme
## 定义
* Attr：属性，风格样式的最小单元。

例如：

```
<attr name="width" format="dimension"/>
<declare-styleable> 表示一个属性组
```



* style：风格样式，一系列 Attr 的集合，用来定义一个 View 的样式，

例如：

```
<style name="CustomView">
    <item name="width">10dp</item>
</style>
```




* theme：主题，和 style 一样，包含一种或多种属性的集合，都是资源，定义格式一样，存放在res/values/styles.xml 文件夹下，创建节点是 resources-style-item。
不同的是，theme作用于 Activity 或整个 Application 上，必须在 AndroidManifest.xml 或代码中声明，而 style 作用在单独的 View 上，例如自定义 View，或者 TextView 等。

例如：

```
<application android:theme="@style/CustomTheme">

<style name="child" parent="@android:style/Dialog">
```




* @符号 表明我们引用的资源是定义过的。这style可以在对应的style.xml中找到。 




* ?问号 表明我们引用的资源的值在主题当中定义过。比如 color.xml 中定义 backColor，就可以用 ?backColor 使用。也叫预定义样式



布局中的 layout_width 定义在 /Users/xx/Library/Android/sdk/platforms/android-Q/data/res/values/attrs.xml

```
<declare-styleable name="ViewGroup_Layout">
    <attr name="layout_width" format="dimension">
        <enum name="fill_parent" value="-1" />
        <enum name="match_parent" value="-1" />
        <enum name="wrap_content" value="-2" />
    </attr>
    <attr name="layout_height" format="dimension">...</attr>
</declare-styleable>
<attr name="textStyle">
    <flag name="normal" value="0" />
    <flag name="bold" value="1" />
    <flag name="italic" value="2" />
</attr>
```



* flag 表示这几个值可以做或运算，比如 bold|italic 表示既加粗也变成斜体，而enum只能让你选择其中一个值。



* 定义 <attr 时，里面的 name 是使用中的 key 值，format 是对应 value 的类型（也可以直接用 value。format 中类型包括有 color 颜色、dimension dp值、reference 可以给key设置引用，例如 @drawable/image、@color/col，也可以直接使用 format="reference|color" 设置引用和具体值。



## 方法含义

Context#obtainStyledAttributes()  -> Resources#obtainStyledAttributes() 四个参数的含义

TypedArray obtainStyledAttributes(
AttributeSet set, // attrs 此 View 在 xml 节点下的属性集合
@StyleableRes int[] attrs, // 给此 View 定义的 attrs
@AttrRes int defStyleAttr,  // 当前主题中的一个属性，它包含对为TypedArray提供默认值的样式资源的引用。 0为默认值。此属性可以在 attrs.xml 中定义，在代码中使用，详细看下面章节。
@StyleRes int defStyleRes) {} // 为TypedArray提供默认值的样式资源的资源标识符，仅在defStyleAttr为0或在主题中找不到时使用。 0为默认值。

注意：这四个属性的级别从前到后依次递减。

```
class RView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : View(context, attrs, defStyleAttr) {
    constructor(context: Context):this(context,null,R.attr.rView)
    constructor(context: Context, attrs:AttributeSet?):this(context,attrs,R.attr.rView)
}
class RView : View {
    constructor(context: Context):this(context,null,0){}
    constructor(context: Context,attributeSet: AttributeSet?) : this(context,attributeSet,0) {}
    constructor(context: Context,attributeSet: AttributeSet?,defStyle:Int):super(context,attributeSet,defStyle){
        context.obtainStyledAttributes(attributeSet, R.styleable.RView,0, defStyle).apply {
            setRadius(getDimension(R.styleable.RView_rv_radius, 0f))
            recycle()
        }
    }
    fun setRadius(color: Int){}
}
 
class RView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = R.attr.AttrsReference
) : View(context, attrs, defStyleAttr) {
    init {
        context.obtainStyledAttributes(attrs, R.styleable.RView, defStyleAttr, R.style.RView).apply {
            setRadius(getDimension(R.styleable.RView_rv_radius, 0f))
            recycle()
        }
    }
    fun setRadius(color: Int){}
}
```



## 使用自定义属性

```
<declare-styleable name="RView">
    <attr name="rv_radius" format="dimension"/>
</declare-styleable>

class RView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = R.attr.AttrsReference
) : View(context, attrs, defStyleAttr) {
    init {
        context.obtainStyledAttributes(attrs, R.styleable.RView, defStyleAttr, R.style.RView).apply {
            setRadius(getDimension(R.styleable.RView_rv_radius, 0f))
            recycle()
        }
    }
}

val theme = context.getTheme()
val dogArray = theme.obtainStyledAttributes(attrs, R.styleable.RView, defStyleAttr, defStyleRes)
val name = dogArray.getDimension(R.styleable.RView_rv_radius)
dogArray.recycle()
```



* 如果要在代码中给此 Activity 下面的 View 设置状态，需要注意的是，给自定义 View 的构造参数 defaultStyleAttribute: Int = R.attr.AttrsReference 给定一个默认指定 reference，在 Activity 对应的 style 里面写入这个引用对应的 attrs。

例如 

```
在 attrs.xml 中：
<attr name="AttrsReference" format="reference"/>

在 style.xml 中：
<style name="StyleViewTestReference">
    <item name="AttrsReference">@style/StyleTestSpecific</item>
</style>
<style name="StyleTestSpecific">
    <item name="backColor">#5500FF00</item>
</style>
```



* Activity 中在 setContentView() 之前设置 setTheme()。



* Fragment 设置这个页面的 Theme
```
override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val contextThemeWrapper = ContextThemeWrapper(activity, R.style.AcView)
    val themeWrapperInflater = inflater?.cloneInContext(contextThemeWrapper)
    return themeWrapperInflater?.inflate(R.layout.fragment_layout, container, false)
}
```
