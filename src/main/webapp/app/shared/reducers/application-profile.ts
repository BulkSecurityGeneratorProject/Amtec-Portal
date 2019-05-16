import axios from 'axios';

import { SUCCESS } from 'app/shared/reducers/action-type.util';

export const ACTION_TYPES = {
  GET_PROFILE: 'applicationProfile/GET_PROFILE',
  HIDE_MENU: 'applicationProfile/HIDE_MENU',
  SHOW_MENU: 'applicationProfile/SHOW_MENU'
};

const initialState = {
  ribbonEnv: '',
  inProduction: true,
  isSwaggerEnabled: false,
  menuHidden: false
};

export type ApplicationProfileState = Readonly<typeof initialState>;

export default (state: ApplicationProfileState = initialState, action): ApplicationProfileState => {
  switch (action.type) {
    case SUCCESS(ACTION_TYPES.GET_PROFILE):
      const { data } = action.payload;
      return {
        ...state,
        ribbonEnv: data['display-ribbon-on-profiles'],
        inProduction: data.activeProfiles.includes('prod'),
        isSwaggerEnabled: data.activeProfiles.includes('swagger')
      };
    case ACTION_TYPES.HIDE_MENU:
      return {
        ...state,
        menuHidden: action.payload
      };
    case ACTION_TYPES.SHOW_MENU:
      return {
        ...state,
        menuHidden: action.payload
      };
    default:
      return state;
  }
};

export const getProfile = () => ({
  type: ACTION_TYPES.GET_PROFILE,
  payload: axios.get('management/info')
});

export const hideMenu = () => ({
  type: ACTION_TYPES.HIDE_MENU,
  payload: true
});

export const showMenu = () => ({
  type: ACTION_TYPES.SHOW_MENU,
  payload: false
});
