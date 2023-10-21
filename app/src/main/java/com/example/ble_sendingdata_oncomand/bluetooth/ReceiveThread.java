package com.example.ble_sendingdata_oncomand.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ReceiveThread extends Thread {
    private BluetoothSocket socket;
    private InputStream inputS;
    private OutputStream outputS;
    private byte[] rBuffer;

    //Відкриваємо поток для предачі інфо
    public ReceiveThread (BluetoothSocket socket) {
        this.socket = socket;
        try{
            inputS = socket.getInputStream();
        } catch (IOException e){

        }
// Тут ми ініціалізували наш об'єкт інпутСтрім, тепер за допомогою нього ми можемо відправляти дані
        try{
            outputS = socket.getOutputStream();
        } catch (IOException e){

        }

    }

    @Override
    public void run() {
        rBuffer = new byte[1024];
        while (true) {
            //Пробуємо зчитати інфо
            try{
                int size = inputS.read(rBuffer);
                //Перетворюємо отримані дані в Стрінг
                /*В дужках ми вказуємо 1) цепочка тексту нашого rBuffer, 2) Звідки ми починаємо(починаємо від 0)
                * 3) Розмір байтів які ми отримали і записали, якщо було передано 3 байта а масив 1024 -
                * то буде вказано розмір 3 байти(тобто скільки байт ми отримали), щоб не збирати постійно
                * наш String з розміром з максимальний розмір масиву в який записуються отримані дані.  */
                String message = new String(rBuffer, 0, size);
                Log.d("MyLog", "Message: " + message);
            }catch (IOException e){
                break;
            }
        }
    }

    //Метод (функція) для відправлення інфо у вигляді масиву байтів

    /*Тут ми будемо передавати масив байтів, тому навіть якщо ми будемо передавати String -
    *то нам потрібно буде перетворювати його на масив байтів. Навіть, якщо це буде просто буква "А"
    * то перетворюємо її в байти і передаємо її в нашу фунцію sendMessage
    * і дані відправляються в мікроконтроллер, якщо ми до нього підключені*/
    public void sendMessage(byte[] byteArray) {
        try{
            outputS.write(byteArray);
        }catch (IOException e){

        }

    }

}
