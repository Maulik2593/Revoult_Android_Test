package revoult.test.maulik.view
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import revoult.test.maulik.R

/**
 * Rate activity
 */
class RateActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.network_layout)
        replaceFragment()
    }

    /**
     * Add fragment to activity
     */
    fun replaceFragment(){
       supportFragmentManager
           .beginTransaction()
           .replace(R.id.container_retro_room, RateFragment())
           .commit()
    }
}
