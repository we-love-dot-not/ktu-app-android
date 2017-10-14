package lt.hacker_house.ktu_ais.views.components

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.grade_item.view.*
import lt.hacker_house.ktu_ais.R
import lt.hacker_house.ktu_ais.models.RlGradeModel
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

    fun setModel(model: RlGradeModel) {
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
            if (gradePiece != RlGradeModel.EMPTY_MARK) {
                lastMarkIndex = index
            }
            textList.add(gradePieceMut)
        }

        textList.forEachIndexed { index, text ->
            val primColor = getColor(context, R.color.textPrimary)
            val secColor = getColor(context, R.color.textDisabled)
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
