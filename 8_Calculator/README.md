# Calculator

<img src="https://github.com/HYUNJUNEPARK/ImageRepository/blob/master/basicApp/8_Calculator.png" height="400"/>

---
1. <a href = "#content1">ROOM</a></br>
-Room build</br>
-RoomTable</br>
-RoomDao(Data Access Object)</br>
-RoomHelper</br>
-DB 생성 && 사용</br>

2. <a href = "#content2">스레드</a></br>
3. <a href = "#content3">UI 컴포넌트</a></br>
4. <a href = "#content4">SpannableStringBuilder</a></br>
5. <a href = "#content5">확장함수</a></br>

* <a href = "#ref">참고링크</a>
---
><a id = "content1">**1. ROOM**</a></br>


(0)어노테이션 프로세싱(Annotation Processing API)</br>
-클래스명, 변수명 위에 '@명령어' 를 사용하는 것으로 명령어가 컴파일 시 코드로 생성되기 때문에 실행 시 발생할 수 있는 성능 문제가 개선됨</br>
-`id 'kotlin-kapt'` 으로 어노테이션 프로세싱을 코틀린에서 사용 가능</br>
<br></br>

(1)Room build</br>
-쿼리문 이해가 없어도 코드만으로 class 와 RDB 를 매핑하고 컨트롤 할 수 있는 ORM 라이브러리 (Object Relational Mapping)</br>
-class -> ORM(데이터 변환) -> SQLite</br>

```
//build.gradle
//dependencies
var roomVersion = "2.4.2"
implementation("androidx.room:room-runtime:$roomVersion")
kapt("androidx.room:room-compiler:$roomVersion")
```
<br></br>

(2)RoomTable</br>
-DB 테이블의 정보를 담고 있는 데이터 클래스</br>
-@Entity(tableName = "") : 해당 어노테이션이 적용된 클래스를 찾아 테이블로 변환</br>
-@ColumnInfo(name = "") : 컬럼에 대한 정보를 담고 있는 변수 위에 사용됨</br>
-@Ignore : 해당 변수가 테이블과 관계없는 변수라는 것을 나타냄</br>
-@PrimaryKey : 기본키로 `@PrimaryKey(autoGenerate = true)` 으로 자동 생성을 지정할 수 있음</br>

```kotlin
@Entity
data class History (
    @PrimaryKey val uid: Int?,
    @ColumnInfo(name = "expression") val expression: String?,
    @ColumnInfo(name="result") val result: String?
)
```
<br></br>

(3)RoomDao(Data Access Object)</br>
-DB 에 접근 해 쿼리를 실행하는 메서드를 모아둔 인터페이스</br>
-@Dao : DAO 라는 것을 명시</br>
-@Query : 쿼리를 직접 작성하고 실행</br>
-@Insert, @Delete</br>

```kotlin
@Dao
interface HistoryDao {
    @Query("SELECT * FROM history")
    fun getAll(): List<History>

    @Insert
    fun insetHistory(history: History)

    @Query("DELETE FROM history")
    fun deleteAll()

    @Delete
    fun delete(history: History)

    결과 하나만 반환하는 함수
    @Query("SELECT * FROM history WHERE result LIKE :result LIMIT 1")
    fun findByResult(result: String): History
}
```
<br></br>

(4)RoomHelper</br>
-RoomDatabase() 를 상속받는 추상 클래스로 RoomTable, RoomDao 의 정보가 내부에 있음</br>
-Room 라이브러리를 통해 미리 만들어져 있는 코드를 사용할 수 있게 됨</br>
-@Database(entities= , version= , exportSchema=)</br>
-entities : Room 라이브러리가 사용할 테이블 클래스 목록 ex. entities = arrayOf(RoomTable::class)</br>
-version : DB 버전, migration 때 사용됨</br>
-exportSchema : true 면 스키마 정보를 파일로 출력</br>

```kotlin
@Database(entities = [History::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}
```
<br></br>

(5)DB 생성 && 사용</br>
-Room.databaseBuilder(context, class, databaseName)</br>

```kotlin
//생성
private lateinit var db: AppDatabase

db = Room.databaseBuilder(
    applicationContext,
    AppDatabase::class.java,
    "historyDB"
).build()

//사용
db.historyDao().insertHistory(History(null, expressionText, resultText))
db.historyDao().deleteAll()
```

<br></br>
<br></br>

><a id = "content2">**2. 스레드**</a></br>

-네트워크 작업, DB 작업 시 서브 스레드를 만들어서 작업하는 것을 권장</br>
-서브 스레드 블럭에서 UI 요소에 접근 시 서브 스레드 내부 runOnUiThread 블럭에서 작업</br>

```
Thread(Runnable {
    db.historyDao().getAll.reversed().forEach {
        runOnUiThread {
        //...
        }
    }
}).start()
```

<br></br>
<br></br>

><a id = "content3">**3. UI 컴포넌트**</a></br>

(1)메인 액티비티에서 history_row.xml 를 잡아 데이터 세팅하고 activity_main.xml 컴포넌트에 배치시키는 상황</br>

```kotlin
fun historyButtonClicked(view: View) {
    binding.historyLayout.isVisible = true
    binding.historyLinearLayout.removeAllViews() //레이아웃에 남아있을 수 있는 history 를 한번 지워주고 db 에서 history 를 불러와 배치
    Thread(Runnable{
        db.historyDao().getAll().reversed().forEach { historyData ->
        runOnUiThread {
            val historyRowBinding = LayoutInflater.from(this).inflate(R.layout.history_row, null, false)
            historyRowBinding.findViewById<TextView>(R.id.historyExpressionTextView).text = historyData.expression
            historyRowBinding.findViewById<TextView>(R.id.historyResultTextView).text = "= ${historyData.result}"
            binding.historyLinearLayout.addView(historyRowBinding)
        }
    }
    }).start()
}
```
<br></br>

(2)removeAllViews()</br>
-ViewGroup 에서 모든 자식 뷰를 제거할 때 호출</br>
`binding.historyLinearLayout.removeAllViews()`</br>
<br></br>

(3)reversed()</br>
-최신 데이터가 가장 위에 올라옴</br>
`db.historyDao().getAll().reversed().forEach { ... }`</br>

<br></br>
<br></br>

><a id = "content4">**4. SpannableStringBuilder**</a></br>

-문자열에서 특정 문자의 색상, 스타일을 바꾸고 싶을 때 사용하는 클래스</br>
`public void setSpan (Object what, int start, int end, int flags)`</br>

```kotlin
private fun setOperatorColor() {
    val ssb = SpannableStringBuilder(binding.expressionTextView.text)
    ssb.setSpan(
        ForegroundColorSpan(getColor(R.color.green)),
        binding.expressionTextView.text.length -1,
        binding.expressionTextView.text.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    binding.expressionTextView.text = ssb
}
```

<br></br>
<br></br>

><a id = "content5">**5. 확장함수**</a></br>

-함수의 객체를 확장시킬 때 사용하는 함수</br>
-String 클래스에는 데이터가 숫자인지 판단해주는 함수가 없기 때문에 String 타입에 isNumber 를 추가해서 사용</br>

```kotlin
fun String.isNumber(): Boolean {
    return try {
        this.toBigInteger()
        true
    }
    catch (e: NumberFormatException) {
        false
    }
}
```

<br></br>
<br></br>
---

><a id = "ref">**참고링크**</a></br>

Room</br>
https://developer.android.com/jetpack/androidx/releases/room</br>

dropLast()</br>
https://rkdxowhd98.tistory.com/76?category=1134007</br>

SpannableStringBuilder</br>
https://jwsoft91.tistory.com/267</br>

Ripple effect</br>
https://colinch4.github.io/2020-12-03/ripple_effect/</br>

