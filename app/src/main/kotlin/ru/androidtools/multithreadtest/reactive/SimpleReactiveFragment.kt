package ru.androidtools.multithreadtest.reactive

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.reactivex.rxjava3.core.Observable
import ru.androidtools.multithreadtest.databinding.FragmentSimpleReactiveBinding
import java.util.Date
import java.util.Timer
import java.util.TimerTask

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SimpleReactiveFragment : Fragment() {
    private var _binding: FragmentSimpleReactiveBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSimpleReactiveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnStart.setOnClickListener { startTimer() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /* Создаём наблюдатели */
    private fun startTimer() {
        /* 1 */
//        Observable.just(
//            "Один", "Два", "Три", "Четыре",
//            "Пять", "Шесть", "Семь", "Восемь",
//            "Девять", "Десять"
//        ).subscribe(object : Observer<String> {
//            override fun onSubscribe(d: Disposable) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onError(e: Throwable) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onComplete() {
//                TODO("Not yet implemented")
//            }
//
//            override fun onNext(t: String) {
//                TODO("Not yet implemented")
//            }
//
//        })

        /* 2 */
        Observable.create { emitter ->
            Timer().apply {
                schedule(object : TimerTask() {
                    override fun run() {
                        emitter.onNext(Date())
                    }
                }, 0L, 1000L)
            }
        }.subscribe { date -> Log.d(TAG, "Current time: $date") }

        /* 3 */
//        Observable.create { emitter ->
//            Timer().apply {
//                schedule(object : TimerTask() {
//                    override fun run() {
//                        emitter.onNext(Date())
//                    }
//                }, 0L, 1000L)
//            }
//        }
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe { date -> binding.tvTimer.text = date.toString() }
    }

    private companion object {
        val TAG: String = SimpleReactiveFragment::class.java.simpleName
    }
}
