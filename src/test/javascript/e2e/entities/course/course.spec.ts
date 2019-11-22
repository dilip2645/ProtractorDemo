/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { CourseComponentsPage, CourseDeleteDialog, CourseUpdatePage } from './course.page-object';

const expect = chai.expect;

describe('Course e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let courseUpdatePage: CourseUpdatePage;
  let courseComponentsPage: CourseComponentsPage;
  let courseDeleteDialog: CourseDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Courses', async () => {
    await navBarPage.goToEntity('course');
    courseComponentsPage = new CourseComponentsPage();
    await browser.wait(ec.visibilityOf(courseComponentsPage.title), 5000);
    expect(await courseComponentsPage.getTitle()).to.eq('Course');
  });

  it('should load create Course page', async () => {
    await courseComponentsPage.clickOnCreateButton();
    courseUpdatePage = new CourseUpdatePage();
    expect(await courseUpdatePage.getPageTitle()).to.eq('Add Course');
    await courseUpdatePage.cancel();
  });

  it('should create and save Courses', async () => {
    const nbButtonsBeforeCreate = await courseComponentsPage.countDeleteButtons();

    await courseComponentsPage.clickOnCreateButton();
    await promise.all([
      courseUpdatePage.setCourseNameInput('javaaaa'),
      courseUpdatePage.setCourseFeeInput('5'),
      courseUpdatePage.setCourseDurationInput('5')
    ]);
    expect(await courseUpdatePage.getCourseNameInput()).to.eq('javaaaa', 'Expected CourseName value to be equals to courseName');
    expect(await courseUpdatePage.getCourseFeeInput()).to.eq('5', 'Expected courseFee value to be equals to 5');
    expect(await courseUpdatePage.getCourseDurationInput()).to.eq('5', 'Expected courseDuration value to be equals to 5');
    await courseUpdatePage.save();
    expect(await courseUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    // expect(await courseComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should load edit Course page', async () => {
    await courseComponentsPage.clickOnLastDeleteButton();
    courseUpdatePage = new CourseUpdatePage();
    await browser.wait(ec.visibilityOf(courseUpdatePage.pageTitle), 5000);
    expect(await courseUpdatePage.getPageTitle()).to.eq('Edit Course');
    await courseUpdatePage.cancel();
  });

  // it('should delete Course ', async () => {
  //   // const nbButtonsBeforeCreate = await courseComponentsPage.countDeleteButtons();
  //      await courseComponentsPage.clickOnLastDeleteButton();
  //       courseDeleteDialog = new CourseDeleteDialog();
  //      // await browser.wait(ec.visibilityOf(CourseDeleteDialog.pagetitle), 5000);
  //      expect(await courseDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Cource?');
  //    // await courseDeleteDialog.clickOnConfirmButton();
  //    });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
