# SlideShowApp

<img src="https://github.com/HYUNJUNEPARK/ImageRepository/blob/master/basicApp/4_SlideShowApp.png" height="400"/>

---
1. <a href = "#content1">screenOrientation</a></br>
2. <a href = "#content3">SAF(Storage Access Framework)</a></br>
3. <a href = "#content4">Permission</a></br>
* <a href = "#ref">참고링크</a>
---
><a id = "content1">**1. screenOrientation**</a></br>

```xml
/*AndroidManifest.xml
android:screenOrientation="portrait" //액티비티를 portrait(세로)로 고정
android:screenOrientation="landscape"> //액티비티를 landscape(가로)로 고정
*/
<activity
    android:name=".SubFrameActivity"
    android:exported="false"
    android:screenOrientation="landscape"/>
<activity/>
```

<br></br>
<br></br>

><a id = "content3">**2. SAF(Storage Access Framework)**</a></br>

문서 및 이미지 등 각종 파일을 탐색하고 저장하는 작업을 간편하게 하려고 도입한 프레임워크

```kotlin
private fun takePhotosSAF() {
    val intent = Intent(Intent.ACTION_GET_CONTENT)
    intent.type = "image/*"
    resultListener.launch(intent)
}
```
**ACTION_GET_CONTENT**</br>
앱이 단순히 데이터를 읽거나 가져올 때 사용하며 이 방식을 사용하면 앱은 데이터 사본(ex. 이미지 파일)을 가져옴

**ACTION_OPEN_DOCUMENT**</br>
앱이 문서 제공자가 소유한 문서에 장기적, 지속적 액세스 권한을 가지기를 바라는 경우 사용</br>
ex)사용자에게 문서 제공자에 저장된 이미지를 편집할 수 있게 해주는 사진 편집 앱

<br></br>
<br></br>

><a id = "content4">**3. Permission**</a></br>

```kotlin
//Permission.kt
abstract class Permission : AppCompatActivity() {
    abstract fun permissionGranted(requestCode: Int)
    abstract fun permissionDenied(requestCode: Int)

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            permissionGranted(requestCode)
        } else {
            permissionDenied(requestCode)
        }
    }

    fun requirePermissions(permissions: Array<String>, requestCode: Int) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            permissionGranted(requestCode)
        } else {
            val isAllPermissionsGranted = permissions.all { checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED }
            if (isAllPermissionsGranted) {
                permissionGranted(requestCode)
            } else {
                ActivityCompat.requestPermissions(this, permissions, requestCode)
            }
        }
    }
}
```
<br></br>

**사용법**</br>

1. 사용하려는 Activity 에 Permission 상속</br>
`class MainActivity : Permission() { ... }`</br>

2. Permission 을 상속 받은 Activity 에 override fun permissionGranted / override fun permissionDenied
```kotlin
override fun permissionGranted(requestCode: Int) {
    takePhotosSAF()
}

override fun permissionDenied(requestCode: Int) {
    showDenyPopup()
}
```
</br>

3. requestPermissions(permissions, requestCode) 을 호출해 사용
```kotlin
private val permissionArray: Array<String> = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

//...

private fun initAddPhotoButton() {
    binding.addPhotoButton.setOnClickListener {
        requirePermissions(permissionArray, 999)
    }
}
```
<br></br>
<br></br>

---

><a id = "ref">**참고링크**</a></br>

액티비티 화면 가로 또는 세로로 고정하기</br>
https://gozz123.tistory.com/17</br>

SAF</br>
https://codechacha.com/ko/android-storage-access-framework/</br>
https://hooun.tistory.com/76</br>


