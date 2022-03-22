package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null
    lateinit var progressBar: ProgressBar

    override fun onFinishInflate() {
        super.onFinishInflate()
        progressBar = findViewById(R.id.progressBar)

        findViewById<Button>(R.id.button_refresh).setOnClickListener {
            presenter?.onInitComplete()
        }
        findViewById<Button>(R.id.button_cancel).setOnClickListener {
            presenter?.onStopCoroutine()
        }
    }

    override fun populate(fact: Fact, imageResource: ImgResource) {
        findViewById<TextView>(R.id.fact_textView).text = fact.text
        Picasso.get().load(imageResource.fileUrl).into(findViewById<ImageView>(R.id.fact_img))
    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun showLoading() {
        progressBar.toVisible()
    }

    override fun hideLoading() {
        progressBar.toInvisible()
    }
}

interface ICatsView {
    fun populate(fact: Fact, imageResource: ImgResource)
    fun showToast(message: String)
    fun showLoading()
    fun hideLoading()
}