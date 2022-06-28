# HSVPicker for the android view system

HSVPicker is an easy and highly customizable HSV picker library for Android.

- It's a customizable color picker library for Hue, Saturation, Value, and Alpha.
- Resizable slider and panel.
- There are NO dependencies with any android framework's view (100% Custom view)

<img src="static/sample1.gif" width="300" height="517"> | <img src="static/sample2.gif" width="300" height="517">

## Download

You can use Gradle :
```gradle
dependencies {
   implementation 'io.github.naverz.pinocchio:hsvpicker-view:1.0.0'
}
```

## How do I use HSVPicker?

### Basic usage

1. Arrange the pickers on your activity or fragment 
```xml
    <io.github.naverz.hsvpicker.view.AlphaSlider
        android:id="@+id/alpha_slider"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:padding="10dp"
        />

    <io.github.naverz.hsvpicker.view.HueSlider
        android:id="@+id/hue_slider"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:padding="10dp"
        />

    <io.github.naverz.hsvpicker.view.SatValPanel
        android:id="@+id/sat_val_panel"
        android:layout_width="match_parent"
        android:layout_height="200dp"
        android:padding="10dp"
        />

```

2. Connect with the ColorMediator
```kotlin
class MainActivity : AppCompatActivity(), ColorMediator.OnColorChangedListener {

    private val colorMediator = ColorMediator()
    private var samplePanel: MaterialCardView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        colorMediator.alphaSlider = findViewById(R.id.alpha_slider)
        colorMediator.satValPanel = findViewById(R.id.sat_val_panel)
        colorMediator.hueSlider = findViewById(R.id.hue_slider)
        colorMediator.setOnColorChangedListener(this)
        samplePanel = findViewById(R.id.sample_panel)
        samplePanel?.setCardBackgroundColor(colorMediator.currentColor)
    }

    override fun onDestroy() {
        super.onDestroy()
        colorMediator.detach()
    }

    override fun onColorChanged(color: Int) {
        samplePanel?.setCardBackgroundColor(color)
    }

    override fun onColorConfirmed(color: Int) {
		// It will be called when you stop the gesture.
        Toast.makeText(this, "onColorConfirm : $color", Toast.LENGTH_LONG).show()
    }
}
```
### Options
#### For XML
```xml
<!-- Common attributes -->
app:thumbColor="#f0f0f0"
app:thumbRadius="10dp"
app:thumbStrokeColor="#000000"
app:thumbStrokeWidth="3dp"

<!-- Slider attribute -->
app:sliderWidth="30dp"
app:sliderOrientation = "horizontal"
app:sliderCornerRadius = "2dp"

<!-- Panel attribute -->
app:panelCornerRadius = "2dp"
```

#### For code
```kotlin
val slider : AlphaSlider = findViewById(R.id.alpha_slider)
slider.thumbRadius = 13f
slider.thumbColor = Color.parseColor("#ffffff")
slider.thumbStrokeColor = Color.parseColor("#ffffff")
slider.thumbStrokeWidth = 13f
slider.sliderWidth = 30f
slider.sliderOrientation = "horizontal"
slider.sliderCornerRadius = 2f

val panel : SatValPanel = findViewById(R.id.sat_val_panel)
panel.panelCornerRadius = 2f
```

#### Attributes

##### Common attributes

|  Attribute Name  |    Description     |                    Default Value                     |
|:----------------:|:------------------:|:----------------------------------------------------:|
|    thumbColor    |    Thumb color     | null<br />(It will be changed to the selected color) |
| thumbStrokeColor | Thumb stroke color |                       #ffffff                        |
|   thumbRadius    | Thumb radius size  |                         15dp                         |
| thumbStrokeWidth | Thumb stroke with  |                         2sp                          |

##### Slider attributes

|   Attribute Name   |       Description        | Default Value |
|:------------------:|:------------------------:|:-------------:|
|    sliderWidth     |     Width of slider      |     30dp      |
| sliderOrientation  |  Orientation of slider   |  horizontal   |
| sliderCornerRadius | Radius of slider corners |     21dp      |

##### Panel attributes

|  Attribute Name   |       Description       | Default Value |
|:-----------------:|:-----------------------:|:-------------:|
| panelCornerRadius | Radius of panel corners |     21dp      |

### Sample Usage

The sample code is [here!](../app/src/main/kotlin/io/github/naverz/hsvpicker/sample/MainActivityByKotlin.kt)

## Contribution 

Feel free to report any issues or any contributions :).\
Any kind of contribution is welcome!

## Licence