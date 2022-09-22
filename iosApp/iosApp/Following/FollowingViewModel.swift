//
//  FollowingViewModel.swift
//  iosApp
//
//  Created by Vladislav on 22.09.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared
import Combine

class FollowingViewModel : ObservableObject {
    
    @Published public var users:[UserFollowing] = []
    
    var pagingController:PagingFollowingControllerHelper? = nil
    var hasNextPage:Bool = false
    
    init(uuid:String) {
        if (uuid != ""){
        pagingController = PagingFollowingControllerHelper(uuidBlog: uuid)
        }
    }
    
    func fetchFollowing() {
        if (pagingController == nil) {return}
        pagingController!.pagingFollowingController.pagingDataIOS.watch { nullablePagingData in
            guard let list = nullablePagingData?.compactMap({ $0 as? UserFollowing }) else {
                return
            }
            self.users = list
            self.hasNextPage = (self.pagingController!.pagingFollowingController.pager.hasNextPage)
        }
    }
    
    func fetchNextData() {
        if (pagingController == nil || hasNextPage == false) {return}
        self.pagingController!.pagingFollowingController.pager.loadNext()
    }
}
