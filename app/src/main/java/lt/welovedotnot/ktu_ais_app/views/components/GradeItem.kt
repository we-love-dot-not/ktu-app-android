package lt.welovedotnot.ktu_ais_app.views.components

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.grade_item.view.*
import lt.welovedotnot.ktu_ais_app.R
import lt.welovedotnot.ktu_ais_app.models.GradeModel
import android.support.v4.content.ContextCompat.getColor
import babushkatext.BabushkaText



/**
 * Created by simonas on 5/1/17.
 */

class GradeItem: RelativeLayout {
    constructor(context: Context) : super(context) { init() }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { init() }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { init() }

    fun init() {
        inflate(context, R.layout.grade_item, this)
    }

    fun setModel(model: GradeModel) {
        className.text = model.name
        gradeType.text = model.type
        val gradeList = model.mark?.split(";")
        val textList = mutableListOf<String>()
        var lastMarkIndex = -1
        gradeList?.forEachIndexed { index, gradePiece ->
            var gradePieceMut = gradePiece
            if (index < gradeList.size-1) {
                gradePieceMut += " "
            }
            if (gradePiece != GradeModel.EMPTY_MARK) {
                lastMarkIndex = index
            }
            textList.add(gradePieceMut)
        }

        textList.forEachIndexed { index, text ->
            val primColor = getColor(context, android.R.color.primary_text_light)
            val secColor = getColor(context, android.R.color.secondary_text_dark)
            var color = secColor
            if (index == lastMarkIndex) {
                color = primColor
            }

            gradeText.addPiece(BabushkaText.Piece.Builder(text)
                    .textColor(color)
                    .build())
        }
        gradeText.display()
    }
}
