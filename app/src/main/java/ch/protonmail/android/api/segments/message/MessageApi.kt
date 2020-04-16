/*
 * Copyright (c) 2020 Proton Technologies AG
 * 
 * This file is part of ProtonMail.
 * 
 * ProtonMail is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ProtonMail is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ProtonMail. If not, see https://www.gnu.org/licenses/.
 */
package ch.protonmail.android.api.segments.message

import androidx.annotation.WorkerThread
import ch.protonmail.android.api.interceptors.RetrofitTag
import ch.protonmail.android.api.segments.BaseApi
import ch.protonmail.android.api.models.IDList
import ch.protonmail.android.api.models.MoveToFolderResponse
import ch.protonmail.android.api.models.NewMessage
import ch.protonmail.android.api.models.UnreadTotalMessagesResponse
import ch.protonmail.android.api.models.messages.receive.MessagesResponse
import ch.protonmail.android.api.models.messages.send.MessageSendBody
import ch.protonmail.android.api.models.messages.send.MessageSendResponse
import ch.protonmail.android.core.Constants
import retrofit2.Call
import java.io.IOException
import ch.protonmail.android.api.models.messages.receive.MessageResponse
import ch.protonmail.android.api.utils.ParseUtils
import io.reactivex.Observable


class MessageApi (private val service : MessageService) : BaseApi(), MessageApiSpec {
    @Throws(IOException::class)
    override fun fetchMessagesCount(retrofitTag: RetrofitTag): UnreadTotalMessagesResponse {
        return ParseUtils.parse(service.fetchMessagesCount(retrofitTag).execute())
    }

    @Throws(IOException::class)
    override fun messages(location: Int): MessagesResponse? {
        return ParseUtils.parse(service.messages(location, "time", "", "").execute())
    }

    override fun messages(location: Int, retrofitTag: RetrofitTag): MessagesResponse? {
        return ParseUtils.parse(service.messages(location, "time", "", "", retrofitTag).execute())
    }

    @Throws(IOException::class)
    override fun fetchMessages(location: Int, time: Long): MessagesResponse? {
        return if (Constants.MessageLocationType.fromInt(location) == Constants.MessageLocationType.STARRED) {
            ParseUtils.parse(service.fetchStarredMessages(1, time).execute())
        } else ParseUtils.parse(service.fetchMessages(location, time).execute())
    }

    @Throws(IOException::class)
    override fun fetchSingleMessageMetadata(messageId: String): MessagesResponse? {
        return ParseUtils.parse(service.fetchSingleMessageMetadata(messageId).execute())
    }


    @Throws(IOException::class)
    override fun markMessageAsRead(messageIds: IDList) {
        service.read(messageIds).execute()
    }

    @Throws(IOException::class)
    override fun markMessageAsUnRead(messageIds: IDList) {
        service.unRead(messageIds).execute()
    }

    @Throws(IOException::class)
    override fun deleteMessage(messageIds: IDList) {
        service.delete(messageIds).execute()
    }

    @Throws(IOException::class)
    override fun emptyDrafts() {
        service.emptyFolder(Constants.MessageLocationType.ALL_DRAFT.messageLocationTypeValue.toString()).execute()
    }

    @Throws(IOException::class)
    override fun emptySpam() {
        service.emptyFolder(Constants.MessageLocationType.SPAM.messageLocationTypeValue.toString()).execute()
    }

    @Throws(IOException::class)
    override fun emptyTrash() {
        service.emptyFolder(Constants.MessageLocationType.TRASH.messageLocationTypeValue.toString()).execute()
    }

    @Throws(IOException::class)
    override fun emptyCustomFolder(labelId: String) {
        service.emptyFolder(labelId).execute()
    }

    @WorkerThread
    @Throws(Exception::class)
    override fun messageDetail(messageId: String): MessageResponse {
        return ParseUtils.parse(service.messageDetail(messageId).execute())
    }

    @WorkerThread
    @Throws(IOException::class)
    override fun messageDetail(messageId: String, retrofitTag: RetrofitTag): MessageResponse {
        return ParseUtils.parse(service.messageDetail(messageId, retrofitTag).execute())
    }

    @WorkerThread
    @Throws(Exception::class)
    override fun messageDetailObservable(messageId: String): Observable<MessageResponse> {
        return service.messageDetailObservable(messageId)
    }

    @WorkerThread
    @Throws(Exception::class)
    override fun search(query: String, page: Int): MessagesResponse {
        return ParseUtils.parse(service.search(query, page).execute())
    }

    @Throws(IOException::class)
    override fun searchByLabelAndPage(query: String, page: Int): MessagesResponse {
        return ParseUtils.parse(service.searchByLabel(query, page).execute())
    }

    @Throws(IOException::class)
    override fun searchByLabelAndTime(query: String, unixTime: Long): MessagesResponse {
        return ParseUtils.parse(service.searchByLabel(query, unixTime).execute())
    }

    @Throws(IOException::class)
    override fun createDraft(newMessage: NewMessage): MessageResponse? {
        return ParseUtils.parse(service.createDraft(newMessage).execute())
    }

    @Throws(IOException::class)
    override fun updateDraft(messageId: String, newMessage: NewMessage, retrofitTag: RetrofitTag): MessageResponse? {
        return ParseUtils.parse(service.updateDraft(messageId, newMessage, retrofitTag).execute())
    }

    override fun sendMessage(messageId: String, message: MessageSendBody, retrofitTag: RetrofitTag): Call<MessageSendResponse> {
        return service.sendMessage(messageId, message, retrofitTag)
    }

    @Throws(IOException::class)
    override fun unlabelMessages(idList: IDList) {
        service.unlabelMessages(idList).execute()
    }

    @Throws(IOException::class)
    override fun labelMessages(body: IDList): MoveToFolderResponse? {
        return ParseUtils.parse(service.labelMessages(body).execute())
    }


}