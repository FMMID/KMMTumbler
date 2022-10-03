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
    
    var pagingController:PagingFollowingController? = nil
    var hasNextPage:Bool = false
    
    init(pagingController:PagingFollowingController?) {
        self.pagingController = pagingController
    }
    
    func fetchFollowing() {
        if (pagingController == nil) {return}
        pagingController!.pagingDataIOS.watch { nullablePagingData in
            guard let list = nullablePagingData?.compactMap({ $0 as? UserFollowing }) else {
                return
            }
            self.users = list
            self.hasNextPage = (self.pagingController!.pager.hasNextPage)
        }
    }
    
    func fetchNextData() {
        if (pagingController == nil || hasNextPage == false) {return}
        self.pagingController!.pager.loadNext()
    }
}
